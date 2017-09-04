# payconiqapp

Small demo for loading repositories and saving offline data.

## Activities

- SpashActivity: initial page which prompts the user to load data if a connection is available, or view offline stored data
- OnlineActivity: load repositories 15 at a time and add them to a list; when the third item from the end is visible, load the next page
- OfflineActivity: show locally stored repository records

## Data classes

- RepositoryItem: object used to define the structure of a repository item
- RepositoriesAdapter: used to set, update and render repository data in a ~~ListView~~

## Utility classes

- AlertUtil: used for encapsulating alert creation and display logic
- NetworkStateReceiver: extends BroadcastReceiver so updates to the device's connection status are handled by the application
- NetworkUtil: used in conjunction with NetworkStateReceiver to define the functionality for handling connection updates
- RealmUtil: wrapper for the Realm module; logic related to handling offline records
- RequestUtil: online functionality wrapper; handles connection checking, request sending, and request queue initializing
- SettingsUtil: define application-level constants

## Additional information

The flow of the application is as follows:

1 the user starts the application
2 the SplashActivity is displayed
3 any network state change will trigger interface updates (i.e. if a connection is available the 'Load data' button will be visibile; otherwise hidden)
4 if the user clicks the online button, OnlineActivity is displayed
5 the first 15 items are loaded
6 each item is checked to determine if it has been already saved; if not, it is stored locally
7 when the user reaches the third item from the bottom, the next 15 items are loaded
8 the last two steps can be repeated until no new records are available online
9 if the user clicks the offline button, the data stored locally is displayed
