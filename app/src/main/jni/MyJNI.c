//
// Created by student on 5/28/2019.
//
#include "MyJNI.h"

JNIEXPORT jint JNICALL Java_com_example_pnrs1_1projekat_MyNDK_increment
  (JNIEnv *env, jobject obj, jint x){
  return ++x;
  }

JNIEXPORT jdouble JNICALL
Java_com_example_pnrs1_1projekat_MyNDK_convertDegrees(JNIEnv *env, jobject instance, jdouble d,
                                                      jint t) {

    // TODO
    if (t == 0){
        d = (d - 32) * 5 / 9;
        return d;
    }else{
        d = d * 9 / 5 + 32;
        return d;
    }

}