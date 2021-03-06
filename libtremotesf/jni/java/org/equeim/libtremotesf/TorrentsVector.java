/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class TorrentsVector {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected TorrentsVector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TorrentsVector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_TorrentsVector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public long size() {
    return libtremotesfJNI.TorrentsVector_size(swigCPtr, this);
  }

  public long capacity() {
    return libtremotesfJNI.TorrentsVector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtremotesfJNI.TorrentsVector_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return libtremotesfJNI.TorrentsVector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtremotesfJNI.TorrentsVector_clear(swigCPtr, this);
  }

  public void add(Torrent x) {
    libtremotesfJNI.TorrentsVector_add(swigCPtr, this, Torrent.getCPtr(x), x);
  }

  public Torrent get(int i) {
    long cPtr = libtremotesfJNI.TorrentsVector_get(swigCPtr, this, i);
    return (cPtr == 0) ? null : new Torrent(cPtr, true);
  }

  public void set(int i, Torrent val) {
    libtremotesfJNI.TorrentsVector_set(swigCPtr, this, i, Torrent.getCPtr(val), val);
  }

}
