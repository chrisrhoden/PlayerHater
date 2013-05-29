package org.prx.playerhater;

import org.prx.playerhater.wrappers.BoundPlayerHater;
import org.prx.playerhater.util.Config;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class PlayerHater {

	/**
	 * Constant to indicate that the reason for an onSongFinished() call is that
	 * the song played to completion.
	 */
	public static final int SONG_END = 0x0000001;

	/**
	 * Constant to indicate that the reason for an onSongFinished() call is that
	 * the skip button was pressed.
	 */
	public static final int SKIP_BUTTON = 0x0000010;

	/**
	 * Constant to indicate that the reason for an onSongFinished() call is that
	 * there was an error playing the song.
	 */
	public static final int ERROR = 0x0000100;

	/**
	 * Pauses the player.
	 * 
	 * @return true if false if the player is not playing.
	 */
	abstract public boolean pause();

	/**
	 * Stops the player.
	 * 
	 * @return {@code true} if successful, {@code false} if the player is not
	 *         playing or paused.
	 */
	abstract public boolean stop();

	/**
	 * Begins playback of the currently loaded {@linkplain Song}.
	 * 
	 * @return {@code true} if successful, {@code false} if there is no
	 *         {@linkplain Song} loaded.
	 */
	abstract public boolean play();

	/**
	 * Begins playback of the currently loaded {@linkplain Song} at
	 * {@code startTime} in the track.
	 * 
	 * @see {@link IPlayerHater#seekTo(int) seekTo(int)}
	 * @see {@link IPlayerHater#play() play()}
	 * @param startTime
	 *            The time in milliseconds at which to begin playback
	 * @return {@code true} if successful, {@code false} if there is no track
	 *         loaded.
	 */
	abstract public boolean play(int startTime);

	/**
	 * Begins playback of a song at the beginning.
	 * 
	 * @param song
	 *            A {@linkplain Song} to play back.
	 * @return {@code true} if successful, {@code false} if there is a problem.
	 */
	abstract public boolean play(Song song);

	/**
	 * Begins playback of {@code song} at {@code startTime}
	 * 
	 * @see {@link PlayerHater#play(int)}, {@link PlayerHater#play(Song)}
	 * @return {@code true} if successful, {@code false} if there is a problem.
	 */
	abstract public boolean play(Song song, int startTime);

	/**
	 * Moves the playhead to {@code startTime}
	 * 
	 * @param startTime
	 *            The time (in milliseconds) to move the playhead to.
	 * @see {@link PlayerHater#play(int)}
	 * @return {@code true} if successful, {@code false} if there is no song
	 *         loaded in the player.
	 */
	abstract public boolean seekTo(int startTime);

	/**
	 * Puts a song on the end of the play queue.
	 * 
	 * @param song
	 *            The {@linkplain Song} to add to the end of the queue.
	 * @return the queue position of the song, in relation to the playhead. If
	 *         this song has been loaded into the now playing position, this
	 *         will return 0. If the song will be played next, it will return 1,
	 *         and so on.
	 */
	abstract public int enqueue(Song song);

	/**
	 * Moves to a new position in the play queue.
	 * 
	 * @param position
	 *            The position in the queue (1-indexed) to skip to.
	 * @return {@code true} if successful, {@code false} if the {@code position}
	 *         requested was invalid.
	 */
	abstract public boolean skipTo(int position);

	/**
	 * Moves to the next song in the play queue.
	 * <p>
	 * If the player is already playing, it will continue to play with the new
	 * track. If it is not playing, it will move to the next track without
	 * playing.
	 * <p>
	 * If the player has loaded last track, the player will be moved to the
	 * first song in the queue and playback will be stopped, regardless of
	 * whether or not the player is already playing.
	 */
	abstract public void skip();

	/**
	 * Moves back in the play queue.
	 * <p>
	 * If the player is already playing, it will continue to play with the new
	 * track. If it is not playing, it will skip without playing.
	 * <p>
	 * If the playhead is currently fewer than 2 seconds from the beginning of
	 * the track, it will skip to the previous track. Otherwise, it will skip to
	 * the beginning of the currently loaded track.
	 * <p>
	 * If the player has loaded the first track in the queue, it will skip to
	 * the beginning of that track regardless of the playhead's position.
	 */
	abstract public void skipBack();

	/**
	 * Removes all songs from the queue.
	 * <p>
	 * If the player is currently playing, only the currently playing song will
	 * be preserved. All tracks, including those before and after the current
	 * track, will be removed from the queue.
	 * <p>
	 * If the player is not playing, all tracks, including the currently loaded
	 * one, will be removed.
	 */
	abstract public void emptyQueue();

	/**
	 * Gets the location of the playhead in milliseconds.
	 * 
	 * @return the current position of the play head in milliseconds
	 */
	abstract public int getCurrentPosition();

	/**
	 * Gets the duration of the currently loaded Song in milliseconds.
	 * 
	 * @return the duration of the currently loaded Song in milliseconds.
	 */
	abstract public int getDuration();

	/**
	 * Gets the {@linkplain Song} representation of the track that is currently
	 * loaded in the player.
	 * 
	 * @return the {@linkplain Song} of the track that is currently loaded in
	 *         the player.
	 */
	abstract public Song nowPlaying();

	/**
	 * Checks to see if the player is currently playing back audio.
	 * 
	 * @return {@code true} if the player is currently playing, {@code false}
	 *         otherwise.
	 */
	abstract public boolean isPlaying();

	/**
	 * Checks to see if the player is currently loading audio.
	 * 
	 * @return {@code true} if the player is currently loading, {@code false}
	 *         otherwise.
	 */
	abstract public boolean isLoading();

	/**
	 * Gets the state of the currently loaded
	 * {@linkplain android.media.MediaPlayer MediaPlayer}, represented as an
	 * int.
	 * 
	 * @return One of: {@link MediaPlayerWithState#IDLE IDLE},
	 *         {@link MediaPlayerWithState#INITIALIZED INITIALIZED},
	 *         {@link MediaPlayerWithState#PREPARING PREPARING},
	 *         {@link MediaPlayerWithState#PREPARED PREPARED},
	 *         {@link MediaPlayerWithState#STARTED STARTED},
	 *         {@link MediaPlayerWithState#PAUSED PAUSED},
	 *         {@link MediaPlayerWithState#PLAYBACK_COMPLETED
	 *         PLAYBACK_COMPLETED}, {@link MediaPlayerWithState#STOPPED STOPPED}
	 *         , {@link MediaPlayerWithState#END END}, or
	 *         {@link MediaPlayerWithState#ERROR ERROR}
	 */
	abstract public int getState();

	/**
	 * Sets the visible buttons for plugins. It is up to the implementation of
	 * the plugin to honor these settings.
	 * 
	 * @see {@link android.media.RemoteControlClient#setTransportControlFlags(int) }
	 * @param transportControlFlags
	 */
	abstract public void setTransportControlFlags(int transportControlFlags);

	/**
	 * Returns the number of items in the queue.
	 * 
	 * @return the number of items currently in the queue.
	 */
	abstract public int getQueueLength();

	/**
	 * Returns the number of clips in the queue which are at least partially
	 * behind the playhead.
	 * <p>
	 * If the currently enqueued track is stopped at the beginning of the track,
	 * it is not considered in this calculation. If the player is paused or
	 * playing or the playhead is partially over the "now playing" track, it
	 * will be considered as part of this calculation.
	 * 
	 * @return the number of clips in the queue with are at least partially
	 *         behind the playhead.
	 */
	abstract public int getQueuePosition();

	/**
	 * Removes the element at {@code position} from the play queue.
	 * 
	 * @param position
	 *            the (non-zero-indexed) position of the item in the queue to
	 *            remove.
	 * @return true if the operation was successful, false if there are fewer
	 *         than {@code position} items in the play queue.
	 */
	abstract public boolean removeFromQueue(int position);

	/**
	 * Releases the {@linkplain ServiceConnection} which this instance is using
	 * if this instance is backed by a {@linkplain ServiceConnection}
	 * 
	 * @return {@code true} if this instance was backed by a
	 *         {@linkplain ServiceConnection} and should not be used anymore,
	 *         {@code false} otherwise.
	 */
	public boolean release() {
		return false;
	}

	/**
	 * Constructs an {@linkplain Intent} which will start the appropriate
	 * {@linkplain PlayerHaterService} as configured in the project's
	 * AndroidManifest.xml file.
	 * 
	 * @param context
	 * @return An {@link Intent} which will start the correct service.
	 * @throws IllegalArgumentException
	 *             if there is no appropriate service configured in
	 *             AndroidManifest.xml
	 */
	public static Intent buildServiceIntent(Context context) {
		Intent intent = new Intent("org.prx.playerhater.SERVICE");
		intent.setPackage(context.getPackageName());
		Config.attachToIntent(intent);

		if (context.getPackageManager().queryIntentServices(intent, 0).size() == 0) {
			intent = new Intent(context, PlaybackService.class);
			Config.attachToIntent(intent);
			if (context.getPackageManager().queryIntentServices(intent, 0)
					.size() == 0) {
				IllegalArgumentException e = new IllegalArgumentException(
						"No usable service found.");
				String tag = context.getPackageName() + "/PlayerHater";
				String message = "Please define your Playback Service. For help, refer to: https://github.com/PRX/PlayerHater/wiki/Setting-Up-Your-Manifest";
				Log.e(tag, message, e);
				throw e;
			}
		}

		return intent;
	}

	/**
	 * Gets an instance of a {@linkplain BoundPlayerHater} which can be used to
	 * interact with the playback service.
	 * 
	 * Calling this method will also invoke
	 * {@linkplain PlayerHater#configure(Context)} if it has not yet been
	 * called.
	 * 
	 * @since 2.1.0
	 * 
	 * @param context
	 *            The context on which to bind the service.
	 * @return an instance of PlayerHater which one can use to interact with the
	 *         Playback Service.
	 */
	public static PlayerHater bind(Context context) {
		return new BoundPlayerHater(context);
	}
}
