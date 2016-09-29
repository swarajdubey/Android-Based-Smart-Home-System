# FYP

This is the 3rd year group project. Running these codes will obviously not work, not even the android app for the phone and the android wear as the
complete hardware system is also required for full integration.

We designed a context aware smart home system for elderly care. The Intel edison microcontroller is used to control the devices in the house based on all the sensor values
and also interact with the android phone and the cloud.

The app on the android wear is for health monitoring purpose whereby it measures the heart rate of the person and also the total steps taken per day, and this information
is stored in a cloud database. Upon suspicious heart rate and detecting the fall, the android phone makes an automatic phone call to a saved phone number. The android wear is
also used for detecting the context i.e. if the person is resting or walking etc. this data is sent to the intel edison which then automates the house. 

The app on the android phone allows the user to interact with the fan and light via the intel edison, it also retreives information about the average temperature
for the last 2 days from the cloud and also the average number of steps taken for the last 5 days with a sugegstion message. The user can also set the system to
automatic mode on the phone which will then signal the intel edison to vary the light intensity and fan speed according to the environment.

Imporvements needed:
- Making the system work if the battery reaches NIL on the android wear
- The cloud server can be slow sometimes hence the response it slow, thus design a system indepent of the cloud to improve response time
