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

Exact menu names differ by phone brand; record the test phone's exact path here once it arrives.
