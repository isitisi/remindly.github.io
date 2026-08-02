# Remindly

A native Android app (Kotlin + Jetpack Compose) for tasks, events, and reminders. Create a task or an event, attach notes, and optionally set a reminder — Remindly fires a real alarm at that time via `AlarmManager`, ringing full-screen with sound and vibration (Dismiss / Snooze), even if the app was closed or the phone rebooted. All data is stored locally in a Room database; there is no account, server, or sync.

## Features

- Create and manage tasks: title, notes, due date, priority, done state
- Create and manage events: title, location, notes, start/end time
- Optional reminders with exact alarms, full-screen ringing, and notification fallback
- Notes attached to every task and event
- Fully offline, local-only storage

## Building

```bash
./gradlew assembleDebug
```

Requires Android SDK (compileSdk 34, minSdk 26) and JDK 17+.

---

# Privacy Policy

**Effective:** August 3, 2026
**Applies to:** Remindly for Android
**Contact:** xitin71@gmail.com

## The short version

Remindly is a task, event, and reminder app that runs entirely on your phone. It has no account system, no server, and no analytics — so there is nothing for us to collect in the first place.

- No account required
- No data leaves your device
- No ads or trackers
- No data sold, ever

## Where your data lives

Everything you enter — task titles, notes, due dates, priorities, event details, locations, and reminder times — is written to a local database stored only on your device. Remindly does not have a server, so this information is never transmitted to us or to anyone else. We simply have no way to see it.

## Permissions Remindly asks for

Android requires apps to declare permissions up front. Each one below exists to make a specific feature work — none of them are used to collect or transmit data.

| Permission | What it's for |
|---|---|
| `POST_NOTIFICATIONS` | Shows the reminder notification when a task or event is due. |
| `SCHEDULE_EXACT_ALARM` | Lets your reminder ring at the exact minute you set, not "sometime around" it. |
| `RECEIVE_BOOT_COMPLETED` | Re-schedules your pending reminders after you restart your phone, so nothing is silently dropped. |
| `USE_FULL_SCREEN_INTENT` | Lets the alarm screen wake and take over the display, similar to a phone's built-in alarm clock. |
| `VIBRATE` / `WAKE_LOCK` | Vibrates the phone and briefly keeps it awake while an alarm is ringing. |

## Sharing with third parties

There isn't any. Remindly contains no advertising SDKs, no analytics libraries, no crash-reporting services, and no third-party integrations of any kind. Nothing you type is uploaded, synced, or shared — because the app has no capability to do so.

## Retention & deletion

Your data stays on your device for as long as the app is installed. To delete it:

- Delete individual tasks or events from within the app, or
- Clear the app's storage in Android Settings, or
- Uninstall Remindly — this permanently removes its local database along with it.

## A note on Android backups

Remindly allows Android's built-in app backup mechanism. If you have Android's device backup turned on for your Google account, your task and event data may be included in that backup so it can be restored if you reinstall the app or set up a new phone. This backup is managed entirely by Android and Google, governed by your Google Account settings — Remindly itself still never sees or transmits this data. You can turn this off anytime in your phone's Settings → Google → Backup.

## Children's privacy

Remindly is a general-purpose productivity tool, not directed at children, and it does not knowingly collect information from anyone — which makes this straightforward: since nothing is collected from any user, nothing is collected from children either.

## Changes to this policy

If Remindly's data practices ever change — for example, if a future version adds an optional sync feature — this page will be updated first, with a new effective date at the top, before that feature ships.

## Contact

Questions about this policy or how Remindly works can be sent to xitin71@gmail.com.
