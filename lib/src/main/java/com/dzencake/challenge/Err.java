package com.dzencake.challenge;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Error description.
 */
public class Err implements Parcelable {
	public static final int UNKNOWN_ERROR = 0;
	public static final int IO_ERROR = 1;

	/**
	 * The code for this error
	 */
	public final int code;
	/**
	 * The message for this error
	 */
    public final String message;

	/**
	 *
	 * @param code the code for this error
	 * @param message the message for this error
	 */
    public Err(int code, String message) {
        this.code = code;
        this.message = message;
    }

	protected Err(Parcel in) {
		code = in.readInt();
		message = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(code);
		dest.writeString(message);
	}

	public static final Creator<Err> CREATOR = new Creator<Err>() {
		@Override
		public Err createFromParcel(Parcel in) {
			return new Err(in);
		}

		@Override
		public Err[] newArray(int size) {
			return new Err[size];
		}
	};
}
