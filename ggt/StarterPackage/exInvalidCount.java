package ggt.StarterPackage;


/**
* ggt/StarterPackage/exInvalidCount.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Aufgabe2.idl
* Freitag, 25. April 2014 17:43 Uhr MESZ
*/

public final class exInvalidCount extends org.omg.CORBA.UserException
{
  public String s = null;

  public exInvalidCount ()
  {
    super(exInvalidCountHelper.id());
  } // ctor

  public exInvalidCount (String _s)
  {
    super(exInvalidCountHelper.id());
    s = _s;
  } // ctor


  public exInvalidCount (String $reason, String _s)
  {
    super(exInvalidCountHelper.id() + "  " + $reason);
    s = _s;
  } // ctor

} // class exInvalidCount
