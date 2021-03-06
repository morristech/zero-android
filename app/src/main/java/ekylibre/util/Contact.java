package ekylibre.util;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import ekylibre.APICaller.SyncAdapter;

/**************************************
 * Created by pierre on 11/17/16.     *
 * ekylibre.util for zero-android     *
 *************************************/

/*
** Use this class to create contact into mobile phone.
**
** This class need context to work.
** Use setter methods to create your contact using setAccount FIRST
** Once you've set the data you want, use the commit method to put it into mobile phone.
** Clear method will clear the class attributes except context, so you will be able to set a new
** contact without re-instantiate this class
**
**
*/
public class Contact
{
    public static final String TYPE_MAIL = "mail";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_MOBILE = "mobile";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_WEBSITE = "website";

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String homeNumber;
    private String workNumber;
    private String email;
    private String company;
    private String jobTitle;
    private String website;
    private ByteArrayOutputStream photo;
    private ArrayList<ContentProviderOperation> contactParameter;
    private Context mContext;

    public Contact(Context context)
    {
        contactParameter = new ArrayList<>();
        mContext = context;
    }

    public void setAccount(Account account)
    {
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, SyncAdapter.ACCOUNT_TYPE)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, AccountTool.getEmail(account))
                .build());
    }

    public void setPhoto(byte[] photo)
    {
        if (photo == null)
            return;
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.IS_SUPER_PRIMARY, 1)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
                .build()
        );

    }

    public void setPhoto(String photo)
    {
        if (photo == null)
            return;
        this.photo = ImageConverter.createStreamFromBase64(photo);
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.CommonDataKinds.Photo.IS_SUPER_PRIMARY, 1)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, this.photo.toByteArray())
                .build()
        );

        try
        {
            this.photo.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setName(String firstName, String lastName)
    {
        if (firstName == null || lastName == null)
            return;
        this.firstName = firstName;
        this.lastName = lastName;
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        firstName)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                        lastName).build());

    }

    public void setWebsite(String website)
    {
        if (website == null)
            return;
        this.website = website;
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Website.TYPE,
                        ContactsContract.CommonDataKinds.Website.TYPE_WORK)
                .withValue(
                        ContactsContract.CommonDataKinds.Website.URL,
                        website).build());
    }

    public void setMail(String street, String city, String postalCode, String country)
    {
        if (street == null || city == null || postalCode == null || country == null)
            return;
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                        street)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        city)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                        postalCode)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        country).build());
    }

    public void setMobileNumber(String mobileNumber)
    {
        if (mobileNumber == null)
            return;
        this.mobileNumber = mobileNumber;
        contactParameter.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        }

    public void setHomeNumber(String homeNumber)
    {
        if (homeNumber == null)
            return;
        this.homeNumber = homeNumber;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
    }

    public void setWorkNumber(String workNumber)
    {
        if (workNumber == null)
            return;
        this.workNumber = workNumber;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

    }

    public void setEmail(String email)
    {
        if (email == null)
            return;
        this.email = email;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());
    }

    public void setOrganization(String company, String jobTitle)
    {
        if (company == null || jobTitle == null)
            return;
        this.company = company;
        this.jobTitle = jobTitle;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .build());
    }

    /*
    ** Alternative way to add organization without jobTitle
    */
    public void setOrganization(String company)
    {
        if (company == null)
            return;
        this.company = company;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .build());
    }

    public void deleteContact(String firstName, String lastName, Context context)
    {
        ContentResolver contentResolver = context.getContentResolver();

        Log.d("AZERTY", "params = " + firstName + "       " + lastName);
        contactParameter.clear();
        contactParameter.add(
                ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
                                        + " LIKE " + "\"" + firstName + "\""
                                        + " AND "
                                        + ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
                                        + " LIKE " + "\"" + lastName + "\"",
                                null)
                        .build());
    }

    public void commit()
    {
        if (contactParameter == null)
            return;
        try
        {
            mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, contactParameter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void clear()
    {
        firstName = null;
        lastName = null;
        mobileNumber = null;
        homeNumber = null;
        workNumber = null;
        email = null;
        company = null;
        jobTitle = null;
        photo = null;
        website = null;
        contactParameter.clear();
    }


    /* ****************
    **    Getters
    ******************/


    public String getMobileNumber()
    {
        return (mobileNumber);
    }

    public String getHomeNumber()
    {
        return (homeNumber);
    }

    public String getWorkNumber()
    {
        return (workNumber);
    }

    public String getEmail()
    {
        return (email);
    }

    public String getCompany()
    {
        return (company);
    }

    public String getWebsite()
    {
        return (website);
    }

    public String getFirstName()
    {
        return (firstName);
    }

    public String getLastName()
    {
        return (lastName);
    }
}

