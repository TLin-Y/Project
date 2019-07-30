/*
 * COMP6231 A1
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package functions;

/**
 * Define 3 types for events by enum
 */
public enum EventType {
CONFERENCES,SEMINARS,TRADESHOWS;

	public static  boolean isValidEventType(final String eventype) {
		for (EventType e : EventType.values()) {
			if (e.toString().equalsIgnoreCase(eventype))
				return true;
		}
		return false;
	}

}
