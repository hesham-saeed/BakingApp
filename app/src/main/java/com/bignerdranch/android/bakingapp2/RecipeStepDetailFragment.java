package com.bignerdranch.android.bakingapp2;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment{
    private static final String ARGS_LONG_DESCRIPTION = "long_description";
    private static final String ARGS_VIDEO_URL = "videos_url";
    private static final String ARGS_STEP_POSITION = "step_position";
    private static final String ARGS_STEPS_COUNT = "steps_number";

    private static final String EXTRA_PLAYER_POSITION = "player_position";
    private static final String EXTRA_PLAY_WHEN_READY = "play_when_ready";
    private static final String EXTRA_WINDOW_INDEX = "window_index";


    @BindView(R.id.exo_player_view) PlayerView mPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    @BindView(R.id.tv_recipe_description) TextView mLongDescriptionTextView;
    private String mVideoURL;
    private String mLongDescription;
    private int mPosition;
    private int mStepsCount;
    private long playerPosition;
    private boolean playWhenReady;
    private int currentWindowIndex;
    private Callbacks mCallbacks;
    @BindView(R.id.fab_recipe_next_step) FloatingActionButton nextButton;
    @BindView(R.id.fab_recipe_previous_step) FloatingActionButton previousButton;
    @BindView(R.id.empty_video_place_holder) TextView mPlaceHolder;

    public static Fragment newInstance(String longDescription, String videoURL, int position, int stepsNumber){
        Bundle args = new Bundle();
        args.putString(ARGS_LONG_DESCRIPTION, longDescription);
        args.putString(ARGS_VIDEO_URL, videoURL);
        args.putInt(ARGS_STEP_POSITION, position);
        args.putInt(ARGS_STEPS_COUNT, stepsNumber);

        Fragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLongDescription = getArguments().getString(ARGS_LONG_DESCRIPTION);
        mVideoURL = getArguments().getString(ARGS_VIDEO_URL);
        mPosition = getArguments().getInt(ARGS_STEP_POSITION);
        mStepsCount = getArguments().getInt(ARGS_STEPS_COUNT);
    }



    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    private void releasePlayer(){
        if (mSimpleExoPlayer != null){
            playerPosition = mSimpleExoPlayer.getCurrentPosition();
            currentWindowIndex = mSimpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 ){
            initializePlayer(mVideoURL, playerPosition);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mSimpleExoPlayer == null)){
            initializePlayer(mVideoURL, playerPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_PLAYER_POSITION,playerPosition);
        outState.putInt(EXTRA_WINDOW_INDEX, currentWindowIndex);
        outState.putBoolean(EXTRA_PLAY_WHEN_READY, playWhenReady);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks{
        void onStepForward(int position);
        void onStepBackward(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        if(savedInstanceState != null){
            playerPosition = savedInstanceState.getLong(EXTRA_PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(EXTRA_PLAY_WHEN_READY);
            currentWindowIndex = savedInstanceState.getInt(EXTRA_WINDOW_INDEX);
        }

        ButterKnife.bind(this,v);

        mLongDescriptionTextView.setText(mLongDescription);
        nextButton.setImageResource(R.drawable.ic_action_next);
        previousButton.setImageResource(R.drawable.ic_action_prev);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onStepForward(mPosition+1);
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onStepBackward(mPosition-1);
            }
        });
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recipe Demo");
        if (mPosition > 0)
            previousButton.setVisibility(View.VISIBLE);
        if (mPosition == mStepsCount-1)
            nextButton.setVisibility(View.INVISIBLE);

        //if it's a mobile and in landscape mode, setup the landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (!(getResources().getConfiguration().smallestScreenWidthDp >= 600))
                setupLandscape();
        }


        //if it's a tablet remove the next and previous floating action buttons
        if(getResources().getConfiguration().smallestScreenWidthDp >= 600){
            nextButton.setVisibility(View.INVISIBLE);
            previousButton.setVisibility(View.INVISIBLE);
        }

        //handling an empty video
        initializePlayer(mVideoURL, playerPosition);
        return v;
    }

    private void initializePlayer(String videoURL, long position){

        if (mVideoURL != null && !TextUtils.isEmpty(mVideoURL)) {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            mPlayerView.setPlayer(mSimpleExoPlayer);
            mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
            mSimpleExoPlayer.seekTo(currentWindowIndex, playerPosition);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory
                    (getActivity(), Util.getUserAgent(getActivity(), "BakingApp2"));
            Uri uri = Uri.parse
                    (mVideoURL);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);

            mSimpleExoPlayer.prepare(videoSource, true, false);

            mPlaceHolder.findViewById(R.id.empty_video_place_holder).setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            //
        } else {
            mPlaceHolder.findViewById(R.id.empty_video_place_holder).setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    //setting up the video to take the full width of the screen and hiding the toolbar in case
    //the user rotates the mobile
    private void setupLandscape(){
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
        params.width = params.MATCH_PARENT;
        params.height = params.MATCH_PARENT;
        params.setMargins(0,0,0,0);
        mPlayerView.setLayoutParams(params);
        nextButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
        mLongDescriptionTextView.setVisibility(View.INVISIBLE);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setupLandscape();
        } else {
            ConstraintLayout.LayoutParams params =
                    (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = 0;
            params.height = 0;
            params.setMargins(8,0,8,0);
            nextButton.setVisibility(View.VISIBLE);

            if (mPosition > 0)
                previousButton.setVisibility(View.VISIBLE);

            mLongDescriptionTextView.setVisibility(View.VISIBLE);
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        }
    }
}
