/**
 * <copyright>
 * </copyright>
 *
 * $Id: TriggerIntermediateMultiple.java,v 1.1 2009/12/22 06:18:20 kmyu Exp $
 */
package net.smartworks.server.engine.process.xpdl.xpdl2;

import java.io.Serializable;

import commonj.sdo.Sequence;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trigger Intermediate Multiple</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultMessage <em>Trigger Result Message</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerTimer <em>Trigger Timer</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultError <em>Result Error</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultCompensation <em>Result Compensation</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerRule <em>Trigger Rule</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultLink <em>Trigger Result Link</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getAny <em>Any</em>}</li>
 *   <li>{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getAnyAttribute <em>Any Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @extends Serializable
 * @generated
 */
public interface TriggerIntermediateMultiple extends Serializable
{
  /**
   * Returns the value of the '<em><b>Trigger Result Message</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   *  BPMN: If the Trigger or Result Type is Message then this must be present
   * <!-- end-model-doc -->
   * @return the value of the '<em>Trigger Result Message</em>' containment reference.
   * @see #isSetTriggerResultMessage()
   * @see #unsetTriggerResultMessage()
   * @see #setTriggerResultMessage(TriggerResultMessage)
   * @generated
   */
  TriggerResultMessage getTriggerResultMessage();

  /**
   * Sets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultMessage <em>Trigger Result Message</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Result Message</em>' containment reference.
   * @see #isSetTriggerResultMessage()
   * @see #unsetTriggerResultMessage()
   * @see #getTriggerResultMessage()
   * @generated
   */
  void setTriggerResultMessage(TriggerResultMessage value);

  /**
   * Unsets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultMessage <em>Trigger Result Message</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetTriggerResultMessage()
   * @see #getTriggerResultMessage()
   * @see #setTriggerResultMessage(TriggerResultMessage)
   * @generated
   */
  void unsetTriggerResultMessage();

  /**
   * Returns whether the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultMessage <em>Trigger Result Message</em>}' containment reference is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Trigger Result Message</em>' containment reference is set.
   * @see #unsetTriggerResultMessage()
   * @see #getTriggerResultMessage()
   * @see #setTriggerResultMessage(TriggerResultMessage)
   * @generated
   */
  boolean isSetTriggerResultMessage();

  /**
   * Returns the value of the '<em><b>Trigger Timer</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * BPMN: If the Trigger Type is Timer then this must be present
   * <!-- end-model-doc -->
   * @return the value of the '<em>Trigger Timer</em>' containment reference.
   * @see #isSetTriggerTimer()
   * @see #unsetTriggerTimer()
   * @see #setTriggerTimer(TriggerTimer)
   * @generated
   */
  TriggerTimer getTriggerTimer();

  /**
   * Sets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerTimer <em>Trigger Timer</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Timer</em>' containment reference.
   * @see #isSetTriggerTimer()
   * @see #unsetTriggerTimer()
   * @see #getTriggerTimer()
   * @generated
   */
  void setTriggerTimer(TriggerTimer value);

  /**
   * Unsets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerTimer <em>Trigger Timer</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetTriggerTimer()
   * @see #getTriggerTimer()
   * @see #setTriggerTimer(TriggerTimer)
   * @generated
   */
  void unsetTriggerTimer();

  /**
   * Returns whether the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerTimer <em>Trigger Timer</em>}' containment reference is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Trigger Timer</em>' containment reference is set.
   * @see #unsetTriggerTimer()
   * @see #getTriggerTimer()
   * @see #setTriggerTimer(TriggerTimer)
   * @generated
   */
  boolean isSetTriggerTimer();

  /**
   * Returns the value of the '<em><b>Result Error</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * BPMN: Must be present if Trigger or ResultType is error.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Result Error</em>' containment reference.
   * @see #isSetResultError()
   * @see #unsetResultError()
   * @see #setResultError(ResultError)
   * @generated
   */
  ResultError getResultError();

  /**
   * Sets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultError <em>Result Error</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Result Error</em>' containment reference.
   * @see #isSetResultError()
   * @see #unsetResultError()
   * @see #getResultError()
   * @generated
   */
  void setResultError(ResultError value);

  /**
   * Unsets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultError <em>Result Error</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetResultError()
   * @see #getResultError()
   * @see #setResultError(ResultError)
   * @generated
   */
  void unsetResultError();

  /**
   * Returns whether the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultError <em>Result Error</em>}' containment reference is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Result Error</em>' containment reference is set.
   * @see #unsetResultError()
   * @see #getResultError()
   * @see #setResultError(ResultError)
   * @generated
   */
  boolean isSetResultError();

  /**
   * Returns the value of the '<em><b>Result Compensation</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * BPMN: Must be present if if Trigger or ResultType is Compensation.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Result Compensation</em>' containment reference.
   * @see #isSetResultCompensation()
   * @see #unsetResultCompensation()
   * @see #setResultCompensation(ResultCompensation)
   * @generated
   */
  ResultCompensation getResultCompensation();

  /**
   * Sets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultCompensation <em>Result Compensation</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Result Compensation</em>' containment reference.
   * @see #isSetResultCompensation()
   * @see #unsetResultCompensation()
   * @see #getResultCompensation()
   * @generated
   */
  void setResultCompensation(ResultCompensation value);

  /**
   * Unsets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultCompensation <em>Result Compensation</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetResultCompensation()
   * @see #getResultCompensation()
   * @see #setResultCompensation(ResultCompensation)
   * @generated
   */
  void unsetResultCompensation();

  /**
   * Returns whether the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getResultCompensation <em>Result Compensation</em>}' containment reference is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Result Compensation</em>' containment reference is set.
   * @see #unsetResultCompensation()
   * @see #getResultCompensation()
   * @see #setResultCompensation(ResultCompensation)
   * @generated
   */
  boolean isSetResultCompensation();

  /**
   * Returns the value of the '<em><b>Trigger Rule</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * BPMN: if the TriggerType is Rule then this must be present.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Trigger Rule</em>' containment reference.
   * @see #isSetTriggerRule()
   * @see #unsetTriggerRule()
   * @see #setTriggerRule(TriggerRule)
   * @generated
   */
  TriggerRule getTriggerRule();

  /**
   * Sets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerRule <em>Trigger Rule</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Rule</em>' containment reference.
   * @see #isSetTriggerRule()
   * @see #unsetTriggerRule()
   * @see #getTriggerRule()
   * @generated
   */
  void setTriggerRule(TriggerRule value);

  /**
   * Unsets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerRule <em>Trigger Rule</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetTriggerRule()
   * @see #getTriggerRule()
   * @see #setTriggerRule(TriggerRule)
   * @generated
   */
  void unsetTriggerRule();

  /**
   * Returns whether the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerRule <em>Trigger Rule</em>}' containment reference is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Trigger Rule</em>' containment reference is set.
   * @see #unsetTriggerRule()
   * @see #getTriggerRule()
   * @see #setTriggerRule(TriggerRule)
   * @generated
   */
  boolean isSetTriggerRule();

  /**
   * Returns the value of the '<em><b>Trigger Result Link</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * BPMN: if the Trigger or Result Type is Link then this must be present.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Trigger Result Link</em>' containment reference.
   * @see #isSetTriggerResultLink()
   * @see #unsetTriggerResultLink()
   * @see #setTriggerResultLink(TriggerResultLink)
   * @generated
   */
  TriggerResultLink getTriggerResultLink();

  /**
   * Sets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultLink <em>Trigger Result Link</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Result Link</em>' containment reference.
   * @see #isSetTriggerResultLink()
   * @see #unsetTriggerResultLink()
   * @see #getTriggerResultLink()
   * @generated
   */
  void setTriggerResultLink(TriggerResultLink value);

  /**
   * Unsets the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultLink <em>Trigger Result Link</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetTriggerResultLink()
   * @see #getTriggerResultLink()
   * @see #setTriggerResultLink(TriggerResultLink)
   * @generated
   */
  void unsetTriggerResultLink();

  /**
   * Returns whether the value of the '{@link net.smartworks.server.engine.process.xpdl.xpdl2.TriggerIntermediateMultiple#getTriggerResultLink <em>Trigger Result Link</em>}' containment reference is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Trigger Result Link</em>' containment reference is set.
   * @see #unsetTriggerResultLink()
   * @see #getTriggerResultLink()
   * @see #setTriggerResultLink(TriggerResultLink)
   * @generated
   */
  boolean isSetTriggerResultLink();

  /**
   * Returns the value of the '<em><b>Any</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Any</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Any</em>' attribute list.
   * @generated
   */
  Sequence getAny();

  /**
   * Returns the value of the '<em><b>Any Attribute</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Any Attribute</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Any Attribute</em>' attribute list.
   * @generated
   */
  Sequence getAnyAttribute();

} // TriggerIntermediateMultiple
