/*
 * Copyright (c) 2007, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.opensolaris.os.dtrace;

import java.io.*;
import java.util.EventObject;

/**
 * An event used to pass probe data generated by a DTrace {@link
 * Consumer} to interested listeners.
 *
 * @see ConsumerListener#dataReceived(DataEvent e)
 *
 * @author Tom Erickson
 */
public class DataEvent extends EventObject {
    static final long serialVersionUID = 3068774547474769821L;

    /** @serial */
    private ProbeData probeData;

    /**
     * Creates a {@link ConsumerListener#dataReceived(DataEvent e)
     * dataReceived()} event that conveys data generated by DTrace from
     * a single probe firing.
     *
     * @throws NullPointerException if the given probe data is {@code
     * null}
     */
    public
    DataEvent(Object source, ProbeData generatedData)
    {
	super(source);
	probeData = generatedData;
	validate();
    }

    private final void
    validate()
    {
	if (probeData == null) {
	    throw new NullPointerException("probe data is null");
	}
    }

    /**
     * Gets the data generated by DTrace from a single probe firing.
     *
     * @return non-null probe data generated by DTrace from a single
     * probe firing
     */
    public ProbeData
    getProbeData()
    {
	return probeData;
    }

    private void
    readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException
    {
	s.defaultReadObject();
	// check invariants
	try {
	    validate();
	} catch (Exception e) {
	    InvalidObjectException x = new InvalidObjectException(
		    e.getMessage());
	    x.initCause(e);
	    throw x;
	}
    }

    /**
     * Gets a string representation of this event useful for logging and
     * not intended for display.  The exact details of the
     * representation are unspecified and subject to change, but the
     * following format may be regarded as typical:
     * <pre><code>
     * class-name[property1 = value1, property2 = value2]
     * </code></pre>
     */
    public String
    toString()
    {
	StringBuilder buf = new StringBuilder();
	buf.append(DataEvent.class.getName());
	buf.append("[source = ");
	buf.append(getSource());
	buf.append(", probeData = ");
	buf.append(probeData);
	buf.append(']');
	return buf.toString();
    }
}