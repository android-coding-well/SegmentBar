# SegmentBar
类似ios UISegmentedControl的自定义控件

![image](https://github.com/huweijian5/SegmentBarDemo/blob/master/screenshots/device-2016-09-28-230043.png)

###示例（参考demo）
```
<com.hwj.junmeng.sb.SegmentBar xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/sb_tab"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:sbar_defaultIndex="1"
        app:sbar_padding="10dp"
        app:sbar_borderWidth="1dp"
        app:sbar_textArray="标签1|标签2|标签3" />
```

###开放如下属性进行自定义设置
```
<declare-styleable name="SegmentBar">
        <!--选中背景颜色-->
        <attr name="sbar_focusColor" format="color"></attr>
        <!--未选中背景颜色-->
        <attr name="sbar_normalColor" format="color"></attr>
        <!--标签，多个以“|”分开-->
        <attr name="sbar_textArray" format="string"></attr>
        <!--文字大小-->
        <attr name="sbar_textSize" format="dimension"></attr>
        <!--边框大小-->
        <attr name="sbar_borderWidth" format="dimension"></attr>
        <!--文字上下边距-->
        <attr name="sbar_padding" format="dimension"></attr>
        <!--边框圆角半径-->
        <attr name="sbar_cornerRadius" format="dimension"></attr>
        <!--默认选中标签的下标-->
        <attr name="sbar_defaultIndex" format="integer"></attr>
    </declare-styleable>
```

###直接引用此控件的步骤

Step 1.Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.huweijian5:SegmentBarDemo:1.0.0'
	}
