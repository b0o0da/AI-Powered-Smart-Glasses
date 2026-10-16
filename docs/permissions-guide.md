# Permissions and sideload guide

The app is distributed as a sideloaded APK (Accessibility policy). A sighted helper does this once per phone.

1. Allow installs from unknown sources for the file manager or browser, then install the APK.
2. Open the app and follow the spoken onboarding. Grant: camera, microphone, precise location, send SMS, notifications.
3. **Restricted settings** (Android 13+): Settings → Apps → AVN → ⋮ → *Allow restricted settings*. Needed before step 4.
4. Settings → Accessibility → AVN → turn on.
5. Settings → Notifications → Device & app notifications (Notification access) → AVN → allow.
6. Settings → Apps → AVN → Battery → *Unrestricted* (no battery optimisation).
7. Screen capture consent is asked per session if TD-004 chooses MediaProjection.
8. In the app's Settings, enter the backend URL, the install API key and the emergency contacts (team phones during development).

The Onboarding screen lists every item above with a Grant button, re-checks them each time the app returns to the front, and speaks the reason whenever one is refused. "Start assistant" needs at least one of camera, microphone or location.

If the SMS permission dialog is blocked on a sideloaded build, do step 3 before step 2 (to be confirmed on the test phone).

If the assistant is killed in the background, Android may refuse to restart its camera and microphone access until the app is opened again. Open the app and press "Start assistant".

Exact menu names differ by phone brand; record the test phone's exact path here once it arrives.
