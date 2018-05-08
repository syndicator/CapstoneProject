package info.weigandt.goalacademy.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote implements Parcelable {
// use http://www.jsonschema2pojo.org/
    @SerializedName("quoteText")
    @Expose
    private String quoteText;
    @SerializedName("quoteAuthor")
    @Expose
    private String quoteAuthor;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName("senderLink")
    @Expose
    private String senderLink;
    @SerializedName("quoteLink")
    @Expose
    private String quoteLink;
    public final static Creator<Quote> CREATOR = new Creator<Quote>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        public Quote[] newArray(int size) {
            return (new Quote[size]);
        }

    };

    protected Quote(Parcel in) {
        this.quoteText = ((String) in.readValue((String.class.getClassLoader())));
        this.quoteAuthor = ((String) in.readValue((String.class.getClassLoader())));
        this.senderName = ((String) in.readValue((String.class.getClassLoader())));
        this.senderLink = ((String) in.readValue((String.class.getClassLoader())));
        this.quoteLink = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Quote() {
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderLink() {
        return senderLink;
    }

    public void setSenderLink(String senderLink) {
        this.senderLink = senderLink;
    }

    public String getQuoteLink() {
        return quoteLink;
    }

    public void setQuoteLink(String quoteLink) {
        this.quoteLink = quoteLink;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(quoteText);
        dest.writeValue(quoteAuthor);
        dest.writeValue(senderName);
        dest.writeValue(senderLink);
        dest.writeValue(quoteLink);
    }

    public int describeContents() {
        return 0;
    }
}
