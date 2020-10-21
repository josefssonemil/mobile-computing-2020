# ProxPay

*Link to website: https://emiljosefsson.se/auth*

## What is this?
*ProxPay* is a proof of concept of securing the process of NFC payments using physical credit cards. This is meant to be a solution to the unsecured payments that occur under a certain amount (amount differs between banks) since it does not require any authentication. *ProxPay* solves this by connecting your credit card to your phone using location services and sound frequencies. This enables you to pay as normal while *ProxPay* calulates if you and your phone a close enough to the card for it to be you using it, and then confirms this by confirming a certain frequency that the card emits.

## How to run the code
**Minimum API 28** \
The normal use of the application can be run in both an android emulator and a phyiscal device. In order for the proof of concept, the emulator acts as the card and are the transmitter. Running only one of the devices will result in a declined payment, as intended, since no frequency is emitted.

 In order the have the full experience, in Android Studios ```Run on multiple devices``` with a physical device connected.

### Code Setup

1. Run the code using an emulator
2. In *Logcat*, copy the the emulators Build ID in the first print marked with *****BUILD ID*****
3. Go to ```java/com.exmaple.cardproximity/sound/utils/Constants.java```
4. Replace the ```SENDER``` string with your copied Build ID

### Device Setup
1. Run the app.
2. Enable ```Allow display over other apps```
3. Run the app again.
2. Allow location permission and audio permission when prompted. If no prompt, go to ```Settings``` &rarr; ``` Apps & notifications ``` &rarr; ``` Card Proximity``` and enable the permissions manually.\
 **IMPORTANT:** Make sure to select ```Allow all the time``` for the location permission.
3. Run the app again. Et Voilà!



