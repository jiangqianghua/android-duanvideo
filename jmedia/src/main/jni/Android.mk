LOCAL_PATH:= $(call my-dir)

#ffmpeg lib
include $(CLEAR_VARS)
LOCAL_MODULE := avcodec
LOCAL_SRC_FILES := libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avdevice
LOCAL_SRC_FILES := libavdevice.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avfilter
LOCAL_SRC_FILES := libavfilter.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avformat
LOCAL_SRC_FILES := libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avutil
LOCAL_SRC_FILES := libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample
LOCAL_SRC_FILES := libswresample.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale
LOCAL_SRC_FILES := libswscale.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := speex
LOCAL_SRC_FILES := libspeex.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := x264
LOCAL_SRC_FILES := libx264.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := fdk-aac
LOCAL_SRC_FILES := libfdk-aac.a
include $(PREBUILT_STATIC_LIBRARY)

#Program
include $(CLEAR_VARS)
LOCAL_MODULE    := ffmpegjni
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := sonic.c JPlayMediaJni.c PushAVStream.c
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

LOCAL_LDLIBS += -llog -ldl -lz -g
LOCAL_LDLIBS += -lGLESv1_CM -lGLESv2
LOCAL_LDLIBS += -lOpenSLES
LOCAL_LDLIBS += -ljnigraphics
LOCAL_LDLIBS += -Wl,--no-warn-shared-textrel
LOCAL_DISABLE_FATAL_LINKER_WARNINGS := true

LOCAL_STATIC_LIBRARIES := avdevice avformat avcodec avutil swscale swresample avfilter speex x264 fdk-aac
LOCAL_CFLAGS += -D__STDC_CONSTANT_MACROS
LOCAL_CFLAGS += -DGL_GLEXT_PROTOTYPES

include $(BUILD_SHARED_LIBRARY)