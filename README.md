# BenTV
A simple android video player to show the video stream from an IP Camera.   I have written this because most of the generic
media players expect you to type the media URL each time, or have other issues such as stability problems, or allowing the
screen to blank during playback.   I am using it as an alternative to my Raspberry Pi based video monitor for our disabled son 
(http://www.openseizuredetector.org.uk/?cat=13).

This Android App will play a networked video stream (such as RTSP) from an IP network camera - it works with a D-Link DCS-942
and may well work with others provided both the video and audio streams are compatible with Android.   Note that I have had a
lot of trouble with playing IP camera video streams from Android - the MediaPlayer library crashes without a helpful error 
message if the audio format is not compatible with Android - I have had this on a number of IP cameras.

The App is very simple - there is one configuration setting - the URL of the network stream - provide the full URL such as 
rtsp://guest:guest@192.168.1.6/play2.sdp.   The app will then display the video and play sound, along with a simple status bar
that shows the position in the video stream (in milliseconds) so you can tell it is working.
