# ThemedToggleButtonGroup
![CI](https://github.com/Bryanx/themed-toggle-button-group/workflows/CI/badge.svg)
![API](https://img.shields.io/static/v1?label=API&message=14%2B&color=blue)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT) \
Customisable toggle buttons inside a [FlexboxLayout](https://github.com/google/flexbox-layout).

## Installation
Put this in your project's build.gradle file
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Put this your app's build.gradle file:
```gradle
implementation 'com.github.Bryanx:themed-toggle-button-group:0.2.0'
implementation 'com.google.android:flexbox:2.0.1'
```

## Single selection
![demo](https://github.com/Bryanx/themed-toggle-button-group/blob/master/demo-toggle-cards/assets/basic.gif)
```xml
<nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
    android:id="@+id/time"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:justifyContent="center"> <!-- this attribute is from the underlying FlexboxLayout -->

      <nl.bryanderidder.themedtogglebuttongroup.ThemedButton
          android:id="@+id/btn1"
          android:layout_width="wrap_content"
          android:layout_height="38dp"
          app:toggle_text="5:30PM" />

      <nl.bryanderidder.themedtogglebuttongroup.ThemedButton
          android:id="@+id/btn2"
          android:layout_width="wrap_content"
          android:layout_height="38dp"
          app:toggle_text="7:30PM" />

      <nl.bryanderidder.themedtogglebuttongroup.ThemedButton
          android:id="@+id/btn3"
          android:layout_width="wrap_content"
          android:layout_height="38dp"
          app:toggle_text="8:00PM" />

</nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup>
```

## Multiple selection
![demo](https://github.com/Bryanx/themed-toggle-button-group/blob/master/demo-toggle-cards/assets/labels.gif) \
Declare how many buttons **may** be selected with `toggle_selectableAmount`. \
Declare how many buttons **must** be selected with `toggle_requiredAmount`.
```xml
<nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
    android:id="@+id/tags"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:toggle_selectableAmount="3"
    app:justifyContent="space_between"
    app:flexWrap="wrap">

    <nl.bryanderidder.themedtogglebuttongroup.ThemedButton
        android:id="@+id/tag1"
        android:layout_width="wrap_content"
        android:layout_height="30dp"
        android:layout_margin="3dp"
        app:toggle_text="social" />

    <nl.bryanderidder.themedtogglebuttongroup.ThemedButton
        android:id="@+id/tag2"
        android:layout_width="wrap_content"
        android:layout_height="30dp"
        android:layout_margin="3dp"
        app:toggle_text="music" />
  
    <!-- ... -->
  
  </nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup>
```

## Text + icon selection
![demo](https://github.com/Bryanx/themed-toggle-button-group/blob/master/demo-toggle-cards/assets/togg.gif) \
Use SVG icons to allow the color to be altered.
```xml
<nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
    android:id="@+id/cards"
    android:layout_width="match_parent"
    android:layout_height="155dp"
    android:layout_marginHorizontal="32dp"
    app:alignItems="center">

    <nl.bryanderidder.themedtogglebuttongroup.ThemedButton
        android:id="@+id/card1"
        android:layout_width="0dp"
        android:layout_height="145dp"
        app:layout_flexGrow="1"
        app:toggle_selectedTextColor="@android:color/white"
        app:toggle_selectedBackgroundColor="@color/denim"
        app:toggle_icon="@drawable/replace_with_svg_icon"
        app:toggle_iconGravity="top|center"
        app:toggle_iconPaddingTop="15dp"
        app:toggle_iconPaddingHorizontal="15dp"
        app:toggle_textPaddingBottom="20dp"
        app:toggle_text="Multiple choice"
        app:toggle_textGravity="bottom|center" />

    <!-- ... -->

</nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup>
```

## Contributing
You can contributing by [opening an issue](https://github.com/Bryanx/themed-toggle-button-group/issues) or forking this repository and creating a pull request.

## License
[License for this library](https://github.com/Bryanx/themed-toggle-button-group/blob/master/LICENSE)\
[License for FlexboxLayout](https://github.com/google/flexbox-layout/blob/master/LICENSE)

