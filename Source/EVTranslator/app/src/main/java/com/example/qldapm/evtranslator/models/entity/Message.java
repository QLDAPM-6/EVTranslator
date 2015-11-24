package com.example.qldapm.evtranslator.models.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Tùng Lê on 12-Nov-15.
 */
public class Message
{
    private String m_PhoneNumner;
    public void setPhoneNumner(String PhoneNumner)
    {
        m_PhoneNumner = PhoneNumner;
    }
    public String getPhoneNumner()
    {
        return m_PhoneNumner;
    }

    private String m_Text;
    public void setText(String Text)
    {
        m_Text = Text;
    }
    public String getText()
    {
        return m_Text;
    }

    private Drawable m_Image;
    public void setImage(Drawable Image)
    {
        m_Image = Image;
    }
    public Drawable getImage()
    {
        return m_Image;
    }
}
