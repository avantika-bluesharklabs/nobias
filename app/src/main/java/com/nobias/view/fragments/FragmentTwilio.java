package com.nobias.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.nobias.R;
import com.nobias.businesslogic.viewmodel.fragments.ViewModelTwilio;
import com.nobias.databinding.FragmentTwilioBinding;
import com.nobias.utils.Utils;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.H264Codec;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoCodec;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;
import com.twilio.video.Vp8Codec;

import java.util.ArrayList;
import java.util.List;

import tvi.webrtc.MediaCodecVideoDecoder;
import tvi.webrtc.MediaCodecVideoEncoder;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

/**
 * Created by Smit Shah on Aug 22 2018, 11:38 AM
 * <p>
 * Fragment for attendance log
 */
public class FragmentTwilio extends FragmentBase {
    private ViewModelTwilio mVMTwilio;
    private String mRoomName = "";
    private View mParentView;
    private Room mRoom;
    private boolean mIsFromStripe = false;
    private int previousAudioMode;
    /* The CameraCapturer is a default video capturer provided by Twilio which can
  capture video from the front or rear-facing device camera */
    private CameraCapturer cameraCapturer;

    /* A VideoView receives frames from a local or remote video track and renders them
       to an associated view. */
    private VideoView primaryVideoView, thumbnailVideoView;
    private LocalVideoTrack localVideoTrack;
    private LocalAudioTrack localAudioTrack;
    private RemoteParticipant mRemoteParticipate;
    private AudioManager audioManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentTwilioBinding binding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_twilio, container, false);
        mVMTwilio = new ViewModelTwilio(mApplication);
        binding.setVmTwilio(mVMTwilio);
        mParentView = binding.getRoot();
        mBroadcastManager.registerReceiver(mCameraPermissionReceived, new IntentFilter
                (getResources().getString(R.string.broadcastCameraPermissionTwilio)));
        mIsFromStripe = getArguments().getBoolean(mContext.getResources().getString(R.string
                .bundleTwilioFromStripe));
        mMainActivity.mIsFromStripe = mIsFromStripe;
        initialiseVariables();
        observerEvents();
        return binding.getRoot();
    }

    private void initialiseVariables() {
        primaryVideoView = (VideoView) mParentView.findViewById(R.id.primary_video_view);
        thumbnailVideoView = (VideoView) mParentView.findViewById(R.id.thumbnail_video_view);
        audioManager = (AudioManager) mMainActivity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        checkForHeadphones();

        if (ActivityCompat.checkSelfPermission(mActivity, CAMERA) != PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(mActivity, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            mActivity.requestCameraAndMicPermissions();
        } else {
            // Initialize the camera capturer and start the camera preview
            initiateTwilio();
        }
        // Release the local video track to free native memory resources once you are done
        //localVideoTrack.release();
    }

    private void checkForHeadphones() {
        if(isHeadphonesPlugged())
            audioManager.setSpeakerphoneOn(false);
        else
            audioManager.setSpeakerphoneOn(true);
    }

    private boolean isHeadphonesPlugged() {
        AudioDeviceInfo[]  audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
        for (int i = 0; i < audioDevices.length; i++) {
            AudioDeviceInfo deviceInfo = audioDevices[i];
            if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_USB_HEADSET) {
                return true;
            }
        }
        return false;
    }

    private void initiateTwilio() {
        cameraCapturer = new CameraCapturer(mMainActivity, CameraCapturer.CameraSource.FRONT_CAMERA);
        localVideoTrack = LocalVideoTrack.create(mMainActivity, true, cameraCapturer);
        localAudioTrack = LocalAudioTrack.create(mMainActivity, true, "Microphone");
        primaryVideoView.setMirror(true);
        localVideoTrack.addRenderer(thumbnailVideoView);
        connectToRoom();
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            AudioFocusRequest focusRequest =
                    new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(
                                    i -> { })
                            .build();
            audioManager.requestAudioFocus(focusRequest);
        } else {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }


    private void enableAudioFocus(boolean focus) {
        if (focus) {
            previousAudioMode = audioManager.getMode();
            // Request audio focus before making any device switch.
            requestAudioFocus();
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when playout and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(previousAudioMode);
            audioManager.abandonAudioFocus(null);
        }
    }
    /**
     * Observes events provided by live data single events from login and forgot password view
     * models
     */
    private void observerEvents() {
        mVMTwilio.getLiveEventMute().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            if(localAudioTrack != null) {
                if(localAudioTrack.isEnabled())
                {
                    mVMTwilio.observableMute.set(true);
                    localAudioTrack.enable(false);
                }
                else
                {
                    mVMTwilio.observableMute.set(false);
                    localAudioTrack.enable(true);
                }
            }
        });

        mVMTwilio.getLiveEventDisconnect().observe(this, aVoid -> {
            Utils.hideKeyboard(mMainActivity);
            if(mVMTwilio.observableTwilioConnected.get()) {
                mRoom.disconnect();
                mVMTwilio.observableTwilioConnected.set(false);
                mRemoteParticipate = null;
            }
            else {
                connectToRoom();
            }
        });
    }

    public void connectToRoom() {
        enableAudioFocus(true);
        //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzZiYmU0NzIwODE4NTE0ZGRjNjVlMmIyY2RlMTI2MGQ1LTE1OTUwNzUyNTMiLCJpc3MiOiJTSzZiYmU0NzIwODE4NTE0ZGRjNjVlMmIyY2RlMTI2MGQ1Iiwic3ViIjoiQUM4OWI1NzJjNmI0MTIxZTNlZjc4NjhlNWQ2OWEzNWE1ZCIsImV4cCI6MTU5NTA3ODg1MywiZ3JhbnRzIjp7ImlkZW50aXR5IjoiYXZpX2RhbmdlckB5YWhvby5jb20iLCJ2aWRlbyI6eyJyb29tIjoiYWI4NDBiODMtYTcxOC00ZjNiLWI2NWQtMmQ1M2FjYTRjMzA0In19fQ.McYGrYz1diQoDjPhNMnVuwQRH417utHZ5BWQZEmlepw
        List<LocalAudioTrack> audioTracks = new ArrayList<>();
        audioTracks.add(localAudioTrack);
        List<LocalVideoTrack> videoTracks = new ArrayList<>();
        videoTracks.add(localVideoTrack);

        //Check if H.264 is supported in this device
        boolean isH264Supported = MediaCodecVideoDecoder.isH264HwSupported() &&
                MediaCodecVideoEncoder.isH264HwSupported();

        // Prefer H264 if it is hardware available for encoding and decoding
        VideoCodec videoCodec = isH264Supported ? (new H264Codec()) : (new Vp8Codec());

        ConnectOptions connectOptions = new ConnectOptions.Builder(mApplication.getmTwilioToken())
                .roomName(mRoomName)
                .audioTracks(audioTracks)
                .videoTracks(videoTracks)
                /*.preferAudioCodecs(Collections.singletonList(new IsacCodec()))
                .preferVideoCodecs(Collections.singletonList(videoCodec))
                .encodingParameters(new EncodingParameters(64,800))
                .enableAutomaticSubscription(false)*/
                .build();

        mRoom = Video.connect(mMainActivity, connectOptions, new Room.Listener() {
            @Override
            public void onConnected(Room room) {
                for(int i= 0; i< room.getRemoteParticipants().size(); i++)
                {
                    RemoteParticipant participant = room.getRemoteParticipants().get(i);
                    participant.setListener(remoteParticipantListener());
                }
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                showDialog("Unable to Connect", "Please try again!");
            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onReconnected(@NonNull Room room) {

            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {

            }

            @Override
            public void onRecordingStarted(Room room) {
            }

            @Override
            public void onRecordingStopped(Room room) {
            }

            @Override
            public void onParticipantConnected(Room room, RemoteParticipant participant) {
                Log.i("Room.Listener", participant.getIdentity() + " has joined the room.");
                participant.setListener(remoteParticipantListener());
            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant participant) {
                Log.i("Room.Listener", participant.getIdentity() + " has left the room.");

            }
        });

        // ... Assume we have received the connected callback
        // After receiving the connected callback the LocalParticipant becomes available
        LocalParticipant localParticipant = mRoom.getLocalParticipant();
        //Log.i("LocalParticipant ", localParticipant.getIdentity());

        // Get the first participant from the room
//        RemoteParticipant participant = room.getRemoteParticipants().get(0);
        //Log.i("HandleParticipants", participant.getIdentity() + " is in the room.");
    }

    /* In the Participant listener, we can respond when the Participant adds a Video
    Track by rendering it on screen: */
    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackUnpublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackSubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication, @NonNull RemoteAudioTrack remoteAudioTrack) {

            }

            @Override
            public void onAudioTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onAudioTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication, @NonNull RemoteAudioTrack remoteAudioTrack) {

            }

            @Override
            public void onVideoTrackPublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackUnpublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant participant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
                List<RemoteVideoTrackPublication> videoTracks = participant.getRemoteVideoTracks();
                for(int i =0; i<videoTracks.size(); i++)
                {
                    VideoTrack vt = videoTracks.get(i).getVideoTrack();
                    if(vt.isEnabled())
                    {
                        primaryVideoView.setMirror(false);
                        mVMTwilio.observableTwilioConnected.set(true);
                        remoteVideoTrack.addRenderer(primaryVideoView);
                        mRemoteParticipate = participant;
                    }
                }
            }

            @Override
            public void onVideoTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onVideoTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull RemoteVideoTrack remoteVideoTrack) {

            }

            @Override
            public void onDataTrackPublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication) {

            }

            @Override
            public void onDataTrackUnpublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication) {

            }

            @Override
            public void onDataTrackSubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication, @NonNull RemoteDataTrack remoteDataTrack) {

            }

            @Override
            public void onDataTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onDataTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication, @NonNull RemoteDataTrack remoteDataTrack) {

            }

            @Override
            public void onAudioTrackEnabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackDisabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onVideoTrackEnabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackDisabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }
        };
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private final BroadcastReceiver mCameraPermissionReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean isFromPermission = intent.getBooleanExtra("isFromPermission",false);
            if(isFromPermission)
                initiateTwilio();
            else
                checkForHeadphones();
        }
    };

    @Override
    public void onDestroyView() {
        mBroadcastManager.unregisterReceiver(mCameraPermissionReceived);
        if (localVideoTrack != null)
            localVideoTrack.release();
        if(localAudioTrack != null)
            localAudioTrack.release();
        if(cameraCapturer != null)
            cameraCapturer.stopCapture();
        if (mRoom != null && mRoom.getState() != Room.State.DISCONNECTED) {
            mRoom.disconnect();
        }
        super.onDestroyView();
    }
}
