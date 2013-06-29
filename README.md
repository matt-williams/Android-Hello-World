Android-Hello-World
===================

This is a refactored version of [OpenTok's](https://github.com/opentok) [Android-Hello-World](https://github.com/opentok/Android-Hello-World) sample application.  It extracts the OpenTok publisher and subscriber view function into the com.opentok.view.PublisherView and com.opentok.view.SubscriberView classes.  Each is a standard Android view with additional connect() and disconnect() methods.

## Notes

* This application was built for the Nexus 7, and is locked in landscape orientation. Understandably, many device screens will not like the layout. Feel free to tinker around with the view in res/layout/activity_main.xml
* Please let me know if/how this app crashes on your Android device. We use the Camera and MediaRecorder Android classes and I expect there will be some native issues from device to device
* You may need to create a new token/session to connect. See http://dashboard.tokbox.com/projects
* See the [API reference documentation](http://opentok.github.com/opentok-android-sdk) at the [OpenTok Android SDK project](https://github.com/opentok/opentok-android-sdk) on github. The API for the SDK is not complete; this build is meant to evaluate hardware, not build comprehensive applications.
* AVD/emulator will not work at this time.
* The SDK generates quite a bit of logging. Every logging tag starts with "opentok-", so you should be able to filter it out easily enough while debugging.
