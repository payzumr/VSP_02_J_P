package ggt.KoordinatorPackage;

/**
* ggt/KoordinatorPackage/EAlreadyExistsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Aufgabe2.idl
* Samstag, 26. April 2014 08:57 Uhr MESZ
*/

public final class EAlreadyExistsHolder implements org.omg.CORBA.portable.Streamable
{
  public ggt.KoordinatorPackage.EAlreadyExists value = null;

  public EAlreadyExistsHolder ()
  {
  }

  public EAlreadyExistsHolder (ggt.KoordinatorPackage.EAlreadyExists initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ggt.KoordinatorPackage.EAlreadyExistsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ggt.KoordinatorPackage.EAlreadyExistsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ggt.KoordinatorPackage.EAlreadyExistsHelper.type ();
  }

}
