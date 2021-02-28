# ArcLibrary
[![](https://jitpack.io/v/stelladk/ArcLibrary.svg)](https://jitpack.io/#stelladk/ArcLibrary)

An android library for Layouts with rounded corners

<p align="center">
<img src="preview/example_chat.jpg" alt="example_chat" width="200"/>
</p>


## Install
[![](https://jitpack.io/v/stelladk/ArcLibrary.svg)](https://jitpack.io/#stelladk/ArcLibrary)

Add JitPack in your root build.gradle at the end of repositories

    allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }
        
Add ArcLibrary dependency

    dependencies {
                implementation 'com.github.stelladk:ArcLibrary:0.1.0'
        }


## How to use
Make sure that you have the app namespace declared in your layout

    xmlns:app="http://schemas.android.com/apk/res-auto"

Now you can use the ArcLayout 

    <com.stelladk.arclib.ArcLayout
        android:id="@+id/arcLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:ArcType="none"/>


Every corner can be customised separately.

With the attribute **ArcType** you can specify the corner type you want. The default value is `"none"`.

|`app:TopRightArc="inner"`|`app:TopRightArc="outer"`|
|---|---|
|<img src="preview/inner_crop.jpg" alt="inner arc" width="250">|<img src="preview/outer_y_crop.jpg" alt="outer arc" width="250">|


With the attribute **OuterAxis** you can specify the direction of the outer corners of your layout.

|`app:TopRightOuterAxis="y_axis"`|`app:TopRightOuterAxis="x_axis"`|
|---|---|
|<img src="preview/outer_y_crop.jpg" alt="Y axis" width="250">|<img src="preview/outer_x_crop.jpg" alt="X axis" width="250">|

With the attribute **ArcRadius** you can specify the radius of your corners.

`app:BottomRightRadius="150dp"`

<img src="preview/arc.gif" alt="Arc Radius" width="300">

With the attributes **ArcType**, **OuterAxis** and **ArcRadius** you can specify the default values of all four corners.


---

There are all different kind of shapes that you can build.

### Example 1

    <com.stelladk.arclib.ArcLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        app:ArcType="inner"
        app:TopLeftArc="outer"
        app:BottomRightArc="outer"
        app:OuterAxis="y_axis"
        app:ArcRadius="150dp"/>

<p align="center">
<img src="preview/wave.jpg" alt="Wave example" width="200">
</p>

### Example 2

    <com.stelladk.arclib.ArcLayout
        android:layout_width="match_parent"
        android:layout_height="300dp"

        app:BottomLeftArc="outer"
        app:BottomRightArc="inner"
        app:ArcRadius="150dp"/>

<p align="center">
<img src="preview/header.jpg" alt="Header example" width="200"></p>

### Example 3

    <com.stelladk.arclib.ArcLayout
        android:layout_width="300dp"
        android:layout_height="300dp"

        app:ArcType="inner"
        app:ArcRadius="150dp"/>
<p align="center">
<img src="preview/circle.jpg" alt="Circle example" width="200">
</p>

### Example 4

    <com.stelladk.arclib.ArcLayout
        android:id="@+id/bubble_pink"
        android:layout_width="300dp"
        android:layout_height="300dp"

        app:ArcType="inner"
        app:OuterAxis="y_axis"
        app:ArcRadius="70dp"
        app:BottomRightArc="outer"
        app:BottomRightRadius="100dp"
        android:background="@drawable/pink"/>

    <com.stelladk.arclib.ArcLayout
        android:id="@+id/bubble_white"
        android:layout_width="300dp"
        android:layout_height="300dp"

        app:ArcType="inner"
        app:ArcRadius="100dp"
        app:BottomLeftArc="outer"
        app:BottomLeftRadius="90dp"/>

<p align="center">
<img src="preview/speech_bubbles.jpg" alt="Speech bubbles" width="200"></p>

---

### Photos used
[pattern.png](app/src/main/res/drawable/pattern.png) from <a href="https://pixabay.com/el/users/lucasgrey-679745/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=4032775" target="_blank">Łukasz Siwy</a> via <a href="https://pixabay.com/el/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=4032775" target="_blank">Pixabay</a>
