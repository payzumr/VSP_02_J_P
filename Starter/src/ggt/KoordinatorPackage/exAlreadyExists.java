package ggt.KoordinatorPackage;


/**
* ggt/KoordinatorPackage/exAlreadyExists.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Aufgabe2.idl
* Freitag, 25. April 2014 17:43 Uhr MESZ
*/

public final class exAlreadyExists extends org.omg.CORBA.UserException
{
  public String s = null;

  public exAlreadyExists ()
  {
    super(exAlreadyExistsHelper.id());
  } // ctor

  public exAlreadyExists (String _s)
  {
    super(exAlreadyExistsHelper.id());
    s = _s;
  } // ctor


  public exAlreadyExists (String $reason, String _s)
  {
    super(exAlreadyExistsHelper.id() + "  " + $reason);
    s = _s;
  } // ctor

} // class exAlreadyExists