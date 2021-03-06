# Changelog

## [1.8.5] - 2018-12-22
### Changed
- Updated Qt to version 5.12.0
- Updated OpenSSL to version 1.1.1a
- C++ backend's log entries now has "LibTremotesf" tag
- Updated translations

### Fixed
- Fixed setting files' download state by ticking checkbox
- Fixed setting files' priority

## [1.8.4] - 2018-09-19
### Changed
- Connection error messages are now displayed in a toast

### Fixed
- Fixed loading of system CA certificates
- Fixed action bar height in landscape mode
- Added tag to log entries

## [1.8.3] - 2018-09-11
### Changed
- OpenSSL 1.1.1 is required
- OpenSSL is now built using Clang

## [1.8.2] - 2018-09-02
### Changed
- Migrated from com.android.support support library to androidx
- Migrated from Theme.Design theme to Theme.MaterialComponents
- Tremotesf now uses red as accent color (you can revert it back in settings)

### Fixed
- Fixed crash when disconnecting from server

## [1.8.1] - 2018-08-16
### Fixed
- Fixed crash when disconnecting from server
- "Remove" menu item in torrent's properties activity is now hidden when disconnecting/torrent removal

## [1.8.0] - 2018-08-10
### Added
- Added ability to reannounce torrents
- Added support of Android 9

### Changed
- Added ability set location of multiple torrents at once
- Updated translations

### Fixed
- Crash on saving servers
- Improved support of self-signed certificates
- All network requests are now aborted when disconnecting from server

## [1.7.1] - 2018-07-04
### Changed
- Qt updated to version 5.11.1
- Switched to OpenSSL 1.1
- Native libraries are now build using latest NDK
- Qt and libtremotesf are now build using Clang
- Updated translations

### Fixed
- Not disconnecting when removing last server
- Servers spinner when adding first server / removing last server
- Fixed notifications when connecting to server after disconnect

## [1.7.0] - 2018-06-11
### Added
- Show available free space when adding torrents
- Option to disable finished torrents notifications
- Option to notify about added torrents
- Options to notify about finished/added torrents since last connection to server
- Donation dialog is shown once 2 days after install

### Changed
- Tremotesf now uses the same C++ backend as the Desktop/Sailfish OS version
- Minimum Android version raised to 4.1

### Fixed
- Yandex.Money donate link

## [1.6.3] - 2018-05-09
### Fixed
- Crash when adding torrent file
- Crashes in some weird cases

## [1.6.2] - 2018-05-04
### Changed
- Updated translations

### Fixed
- Crashes with some uncompleted translations
- Crashes on Android versions older than 6.0
- License tab on About screen

## [1.6.1] - 2018-04-30
### Fixed
- Crash when restoring activity with no servers configured

## [1.6.0] - 2018-04-30
### Added
- Support of Android 8.1
- Support of adaptive icons
- Filter torrents by download directory (thanks to beschoenen)
- Donations support (Google Play, PayPal and Yandex.Money)

### Fixed
- Various crashes

## [1.5.1] - 2017-04-13
### Fixed
- Crashes in torrent's files view

## [1.5.0] - 2017-04-09
### Added
- Option to delete torrent's files from hard disk by default when removing torrent
- Menu item for quick enabling/disabling alternative speed limits

### Changed
- Torrents status and tracker filters are restored after app restart and reconnection
- Torrents sort mode and status filter are not reset after disconnecting

### Fixed
- Torrent properties activity doesn't release from memory until files tree is created (memory leak when torrent has large number of files)

## [1.4.2] - 2017-03-07
### Changed
- Trim whitespaces from fields in ServerEditActivity before saving server
- More correct hint text for server's address field

### Fixed
- Correctly check server URL
- Notification actions icons are black on Lollipop+ (for ROMs that doesn't automatically recolor them, e.g. MIUI)
- Show "Disconnect" action on notification when connectiong to server

## [1.4.1] - 2017-02-25
### Changed
- License is now shown in WebView
- Translations are now managed on Transifex.

## [1.4.0] - 2017-02-17
### Added
- Show authentication error
- "About" activity

## [1.3.0] - 2017-02-10
### Added
- Fast scroll for torrent's files

### Changed
- Torrents with large number of files now load a lot faster.

## [1.2.1] - 2017-02-04
### Changed
- Switched to another Bencode library (slightly faster)

## [1.2.0] - 2017-02-03
### Added
- Changing torrents sort order
- Added date is now shown on torrent's details tab

### Changed
- Torrents sort mode is saved in settings

### Fixed
- Torrent status icon with light theme
- Sort by added date now works

## [1.1.0] - 2017-02-01
### Added
- Set torrent location
- Rename torrent's files

### Fixed
- Crash if tracker's address is an IP address

## [1.0.1] - 2017-01-31
### Fixed
- Progress bar visibility on Add torrent file activity
- More correct starting Add torrents activities from other applications
- Fixed one string in Russian translation

## [1.0.0] - 2017-01-29
### Added
- Everything.
