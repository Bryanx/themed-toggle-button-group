# ThemedToggleButtonGroup
![CI](https://github.com/Bryanx/themed-toggle-button-group/workflows/CI/badge.svg)
![API](https://img.shields.io/static/v1?label=API&message=14%2B&color=blue)
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
implementation 'com.github.Bryanx:themed-toggle-button-group:0.1.3'
```

## Single selection
![demo](https://github.com/Bryanx/themed-toggle-button-group/blob/master/demo-toggle-cards/assets/basic.gif) \
```xml
<nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup
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

</nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup>
```

## Multiple selection
![demo](https://github.com/Bryanx/themed-toggle-button-group/blob/master/demo-toggle-cards/assets/labels.gif) \
Declare how many buttons may be selected with `toggle_selectableAmount`.
```xml
<nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup
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
  
  </nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup>
```

## Text + icon selection
![demo](https://github.com/Bryanx/themed-toggle-button-group/blob/master/demo-toggle-cards/assets/togg.gif) \
Use SVG icons to allow the color to be altered.

## License
[License for this library](https://github.com/Bryanx/themed-toggle-button-group/blob/master/LICENSE)\
[License for FlexboxLayout](https://github.com/google/flexbox-layout/blob/master/LICENSE)

