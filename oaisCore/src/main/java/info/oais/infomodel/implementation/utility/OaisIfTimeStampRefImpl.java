/**
 *
 */
package info.oais.infomodel.implementation.utility;

import info.oais.infomodel.interfaces.utility.OaisIfTimeStamp;

/**
 *
 */
public class OaisIfTimeStampRefImpl implements OaisIfTimeStamp {

	/**
	 * m_timeStamp Private value of the timeStamp
	 */
	private long m_timeStamp = 0;

	/**
	 * Get the long value of the timestamp
	 * @return The value of the time stamp.
	 */
	public long getTime() {

		return m_timeStamp;
	}

	/**
	 * Set the value of the time stamp
	 * @param ms The value of the time stamp in ms
	 */
	public void setTime(long ms) {
		m_timeStamp = ms;

	}

	/**
	 * Compare the time stamps.
	 * @param ts TimeStamp to compare.
	 * @return returns a negative integer, zero, or a positive integer
	 *         as this object is less than, equal to, or greater than the specified time.
	 */
	public int compareTo(OaisIfTimeStamp ts) {

		long diff = ts.getTime() - m_timeStamp;
		if (diff > 0) {
			return 1;
		} else if (diff == 0) {
			return 0;
		} else {
			return -1;
		}
	}

}
