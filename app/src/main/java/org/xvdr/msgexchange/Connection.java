/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.xvdr.msgexchange;

public class Connection {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Connection(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Connection obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        msgexchangeJNI.delete_Connection(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    msgexchangeJNI.Connection_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    msgexchangeJNI.Connection_change_ownership(this, swigCPtr, true);
  }

  public Connection() {
    this(msgexchangeJNI.new_Connection(), true);
    msgexchangeJNI.Connection_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public boolean open(String hostname, int port) {
    return msgexchangeJNI.Connection_open(swigCPtr, this, hostname, port);
  }

  public void abort() {
    msgexchangeJNI.Connection_abort(swigCPtr, this);
  }

  public boolean close() {
    return msgexchangeJNI.Connection_close(swigCPtr, this);
  }

  public boolean isOpen() {
    return msgexchangeJNI.Connection_isOpen(swigCPtr, this);
  }

  public boolean isAborting() {
    return msgexchangeJNI.Connection_isAborting(swigCPtr, this);
  }

  public boolean sendRequest(Packet request) {
    return msgexchangeJNI.Connection_sendRequest(swigCPtr, this, Packet.getCPtr(request), request);
  }

  public Packet readResponse() {
    long cPtr = msgexchangeJNI.Connection_readResponse(swigCPtr, this);
    return (cPtr == 0) ? null : new Packet(cPtr, false);
  }

  public Packet transmitMessage(Packet message) {
    long cPtr = (getClass() == Connection.class) ? msgexchangeJNI.Connection_transmitMessage(swigCPtr, this, Packet.getCPtr(message), message) : msgexchangeJNI.Connection_transmitMessageSwigExplicitConnection(swigCPtr, this, Packet.getCPtr(message), message);
    return (cPtr == 0) ? null : new Packet(cPtr, true);
  }

  public void setTimeout(int timeout_ms) {
    msgexchangeJNI.Connection_setTimeout(swigCPtr, this, timeout_ms);
  }

  public String getHostname() {
    return msgexchangeJNI.Connection_getHostname(swigCPtr, this);
  }

  protected void onDisconnect() {
    if (getClass() == Connection.class) msgexchangeJNI.Connection_onDisconnect(swigCPtr, this); else msgexchangeJNI.Connection_onDisconnectSwigExplicitConnection(swigCPtr, this);
  }

  protected void onReconnect() {
    if (getClass() == Connection.class) msgexchangeJNI.Connection_onReconnect(swigCPtr, this); else msgexchangeJNI.Connection_onReconnectSwigExplicitConnection(swigCPtr, this);
  }

}
