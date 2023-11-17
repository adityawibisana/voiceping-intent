# Voiceping Library

Library for Voiceping. Available on Voiceping 3.2.0.

## Getting Started
1. Add to your build.gradle:
```css
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the library
```css
dependencies {
	implementation 'com.github.adityawibisana:voiceping-intent:0.0.2'
}
```
3. Initialize (on your Activity / Application)
```css
	Voiceping.initialize(context = this)
```
4. That's it!

## Actions
List of possible actions that you can do with this.
1. Start PTT. This will start PTT to the current channel.
```css
Voiceping.action.startPTT(context)
```


## States
Listen to Voiceping state, using [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow). Available states:
1. user --> currently logged in user. Code.
```css
	Voiceping.state.user
```
