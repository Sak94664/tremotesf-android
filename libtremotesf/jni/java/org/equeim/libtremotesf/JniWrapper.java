/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class JniWrapper {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected JniWrapper(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(JniWrapper obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_JniWrapper(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public JniWrapper() {
    this(libtremotesfJNI.new_JniWrapper(), true);
  }

  public void setRpc(JniRpc rpc) {
    libtremotesfJNI.JniWrapper_setRpc(swigCPtr, this, JniRpc.getCPtr(rpc), rpc);
  }

}
