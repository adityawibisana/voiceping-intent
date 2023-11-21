
# Voiceping Library

Library for Voiceping, available on Voiceping 3.2.0.

## Getting Started
1. Add the [Jitpack](https://jitpack.io/#adityawibisana/voiceping-intent) repository to your `settings.gradle.kts`: 
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the library
```
dependencies {
	implementation 'com.github.adityawibisana:voiceping-intent:0.0.4'
}
```
3. Initialize on your MainActivity / Application
```css
	Voiceping.initialize(context = this)
```
4. That's it!

## Voiceping Action and State
`Action` is an operation that you can perform with Voiceping. `State` is the current Voiceping state. You can listen to Voiceping's state to determine whether the action you performed executed correctly.

## Actions
List of possible operations that you can perform with Voiceping. Here are the lists that you can do:

### Start PTT
Code: `Voiceping.action.startPTT()`
This will start PTT on the current channel. Mic permission must be enabled on Voiceping. See `Voiceping.state.processor`, and check whether the [processor's state](https://github.com/adityawibisana/voiceping-intent/blob/main/README.md#processor) is `StateRecording` to know that this action was successful. Voiceping must be logged in first. See #Login.

### Stop PTT
Code: `Voiceping.action.stopPTT()`
This will stop PTT.  See `Voiceping.state.processor`, and check whether the [processor's state](https://github.com/adityawibisana/voiceping-intent/blob/main/README.md#processor) is `StateIdle` to know that this action was successful.

### Search Channel
Code: `Voiceping.action.searchChannel(channelName)`
This will search channel and mark that channel as the active channel. You can also search for a user by specifying `channelName` as the user's `displayname`. To check the [channel's state](https://github.com/adityawibisana/voiceping-intent/blob/main/README.md#currentchannel) use `Voiceping.state.currentChannel`.

### Go To Next Channel
Code:`Voiceping.action.goToNextChannel()`
This will jump to the next channel. The next channel is based on the order seen in Voiceping's Favorite screen. To check the [channel's state](https://github.com/adityawibisana/voiceping-intent/blob/main/README.md#currentchannel) use `Voiceping.state.currentChannel`.

### Go to Previous Channel
Code: `Voiceping.action.goToPrevChannel()`
This will jump to the previous channel. The previous channel is based on the order seen in Voiceping's Favorite screen. To check the [channel's state](https://github.com/adityawibisana/voiceping-intent/blob/main/README.md#currentchannel) use `Voiceping.state.currentChannel`.

### Login
Code: `Voiceping.action.login(username, password)`
This allows you to login to Voiceping, using specified `username` and `password`

### Exit
Code: `Voiceping.action.logout()`
This will stop Voiceping. 

## States
Listen to Voiceping state using [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow). Available states:

### User
Code: `Voiceping.state.user`
Currently logged-in user. There are 3 fields:
- `displayname` --> the display name of the user, displayed in the app. You normally use this to search for a specific user.
- `username` --> user's username.
- `fullname` --> user's full name.
 
### Processor
Code: `Voiceping.state.processor`
Current Voiceping processor. The state is either:
#### StatePlaying 
Indicating that Voiceping is currently playing audio, either from live messages or from history.
#### StateRecording
Indicating that Voiceping is currently in a PTT state.
#### StateIdle
Indicating that Voiceping is neither in a playing state nor in a recording state. 

### Current Channel
Code: `Voiceping.state.currentChannel`
Currently active channel.  It has 3 properties:
- `name` --> name of the channel
- `type` --> 0 is Group. 1 is Private (1-1 PTT)
