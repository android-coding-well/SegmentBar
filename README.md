# SegmentBar
类似ios UISegmentedControl的自定义控件

## 介绍说明
多标签切换控件，提供更多接口供用户自定义
![image](https://github.com/huweijian5/SegmentBar/blob/master/screenshots/20170103233539.png)
---
![image](https://github.com/huweijian5/SegmentBar/blob/master/screenshots/device-2017-01-03-232935.mp4_1483457597.gif)
---
## 使用说明
### 示例（参考demo）
```
 <com.hwj.junmeng.sb.SegmentBar xmlns:app="http://schemas.android.com/apk/res-auto"
            android:id="@+id/sb_tab"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:sbar_borderWidth="1dp"
            app:sbar_defaultIndex="1"
            app:sbar_labelPadding="10dp"
            app:sbar_labelArray="标签1|标签2|标签3"
            app:sbar_labelTextSize="20sp"
             />
```
### 开放如下属性进行自定义设置
```
<declare-styleable name="SegmentBar">
        <!--选中背景颜色-->
        <attr name="sbar_focusColor" format="color"></attr>
        <!--未选中背景颜色-->
        <attr name="sbar_normalColor" format="color"></attr>

        <!--标签，多个以“|”分开-->
        <attr name="sbar_labelArray" format="string"></attr>
        <!--标签文字大小-->
        <attr name="sbar_labelTextSize" format="dimension"></attr>
        <!--标签文字上下边距-->
        <attr name="sbar_labelPadding" format="dimension"></attr>
        <!--默认选中标签的下标-->
        <attr name="sbar_defaultIndex" format="integer"></attr>

        <!--边框大小-->
        <attr name="sbar_borderWidth" format="dimension"></attr>
        <!--边框圆角半径-->
        <attr name="sbar_cornerRadius" format="dimension"></attr>


        <!--未读数上边距-->
        <attr name="sbar_unreadMarginTop" format="dimension"></attr>
        <!--未读数右边距-->
        <attr name="sbar_unreadMarginRight" format="dimension"></attr>
        <!--未读数内部边距-->
        <attr name="sbar_unreadPadding" format="dimension"></attr>
        <!--未读数文字大小-->
        <attr name="sbar_unreadTextSize" format="dimension"></attr>
        <!--未读数文字颜色,默认白色-->
        <attr name="sbar_unreadTextColor" format="color"></attr>
        <!--未读数背景颜色,默认红色-->
        <attr name="sbar_unreadBackgroundColor" format="color"></attr>
    </declare-styleable>
```
### 注意
* 如果显示未读数时不需要数字，那么可以通过接口
	public void setUnreadTextSize(float sp);
	public void setUnreadPadding(float dp);综合进行设置红点的大小。
* 设置未读数的接口：public void setLabelUnreadCount(int labelIndex, int unreadCount);
	其中，unreadCount的值为-1时表示显示红点，为0时表示不显示红点，大于0时显示红点和数字。
* 其他接口请看源码或JavaDoc文档
---
## JavaDoc文档

* [在线JavaDoc](https://jitpack.io/com/github/huweijian5/SegmentBar/1.0.2/javadoc/index.html)
* 网址：`https://jitpack.io/com/github/huweijian5/SegmentBar/[VersionCode]/javadoc/index.html`
* 其中[VersionCode](https://github.com/huweijian5/SegmentBar/releases)请替换为最新版本号
* 注意文档使用UTF-8编码，如遇乱码，请在浏览器选择UTF-8编码即可

---
## 引用

* 如果需要引用此库,做法如下：
* Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```	
* and then,add the dependecy:
```
dependencies {
	        compile 'com.github.huweijian5:SegmentBar:latest_version'
}
```
* 其中latest_version请到[releases](https://github.com/huweijian5/SegmentBar/releases)中查看

##注意
* 为了避免引入第三方库导致工程依赖多个版本的问题，如android support库
* 故建议在个人的工程目录下的build.gradle下加入以下变量，具体请看此[build.gradle](https://github.com/huweijian5/SegmentBar/blob/master/build.gradle)
```
ext{
    minSdkVersion = 16
    targetSdkVersion = 25
    compileSdkVersion = 25
    buildToolsVersion = '25.0.1'

    // App dependencies
    supportLibraryVersion = '25.0.1'
    junitVersion = '4.12'
    espressoVersion = '2.2.2'
}
```	
* 请注意，对于此库已有的变量，命名请保持一致



