package ggt.KoordinatorPackage;


/**
* ggt/KoordinatorPackage/EAlreadyExistsHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Aufgabe2.idl
* Samstag, 26. April 2014 08:57 Uhr MESZ
*/

abstract public class EAlreadyExistsHelper
{
  private static String  _id = "IDL:ggt/Koordinator/EAlreadyExists:1.0";

  public static void insert (org.omg.CORBA.Any a, ggt.KoordinatorPackage.EAlreadyExists that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static ggt.KoordinatorPackage.EAlreadyExists extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "s",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (ggt.KoordinatorPackage.EAlreadyExistsHelper.id (), "EAlreadyExists", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static ggt.KoordinatorPackage.EAlreadyExists read (org.omg.CORBA.portable.InputStream istream)
  {
    ggt.KoordinatorPackage.EAlreadyExists value = new ggt.KoordinatorPackage.EAlreadyExists ();
    // read and discard the repository ID
    istream.read_string ();
    value.s = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, ggt.KoordinatorPackage.EAlreadyExists value)
  {
    // write the repository ID
    ostream.write_string (id ());
    ostream.write_string (value.s);
  }

}
