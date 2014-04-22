/*
 Copyright (c) 2014, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Stephen Gold's name may not be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL STEPHEN GOLD BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3utilities.navigation;

import com.jme3.math.Vector3f;
import java.util.Objects;
import java.util.logging.Logger;
import jme3utilities.math.MyVector3f;
import jme3utilities.math.VectorXZ;

/**
 * Immutable arc of a navigation graph: represents a feasible path from one
 * vertex to another vertex. Arcs are unidirectional and need not be straight.
 *
 * @author Stephen Gold <sgold@sonic.net>
 */
public class NavArc
        implements Comparable<NavArc> {
    // *************************************************************************
    // constants

    /**
     * message logger for this class
     */
    final private static Logger logger =
            Logger.getLogger(NavArc.class.getName());
    // *************************************************************************
    // fields
    /**
     * length or cost of this arc's path (arbitrary units, &gt;0)
     */
    private float pathLength;
    /**
     * vertex from which this arc originates (not null)
     */
    private NavVertex fromVertex;
    /**
     * vertex at which this arc terminates (not null)
     */
    private NavVertex toVertex;
    /**
     * direction at the start of this arc (in world space, length=1)
     */
    private Vector3f startDirection;
    /**
     * direction at the start of this arc (in world space, length=1)
     */
    private VectorXZ horizontalDirection;
    // *************************************************************************
    // constructors

    /**
     * Instantiate an arc from one vertex to another.
     *
     * @param fromVertex starting point (not null, distinct from toVertex)
     * @param toVertex endpoint (not null)
     * @param pathLength length or cost (arbitrary units, &gt;0)
     * @param startDirection direction at the start (in world space, length=1,
     * unaffected)
     */
    NavArc(NavVertex fromVertex, NavVertex toVertex, float pathLength,
            Vector3f startDirection) {
        assert fromVertex != null;
        assert toVertex != null;
        assert fromVertex != toVertex : toVertex;
        assert pathLength > 0f : pathLength;
        assert startDirection != null;
        assert startDirection.isUnitVector() : startDirection;

        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        this.pathLength = pathLength;
        this.startDirection = startDirection.clone();
        this.horizontalDirection =
                MyVector3f.horizontalDirection(startDirection);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Access the starting vertex of this arc.
     *
     * @return pre-existing instance
     */
    public NavVertex getFromVertex() {
        return fromVertex;
    }

    /**
     * Read the initial direction of this arc in the XZ plane.
     *
     * @return pre-existing unit vector
     */
    public VectorXZ getHorizontalDirection() {
        return horizontalDirection;
    }

    /**
     * Read the path length (cost) of this arc.
     *
     * @return value (&gt;0, arbitrary units)
     */
    public float getPathLength() {
        return pathLength;
    }

    /**
     * Read the initial direction of this arc.
     *
     * @return new unit vector
     */
    public Vector3f getStartDirection() {
        return startDirection.clone();
    }

    /**
     * Access the endpoint of this arc.
     *
     * @return pre-existing instance
     */
    public NavVertex getToVertex() {
        return toVertex;
    }
    // *************************************************************************
    // Comparable methods

    /**
     * Compare with another arc.
     *
     * @param otherArc (not null, unaffected)
     * @return 0 if the arcs are equivalent
     */
    @Override
    public int compareTo(NavArc otherArc) {
        int result = fromVertex.compareTo(otherArc.getFromVertex());
        if (result != 0) {
            return result;
        }
        result = toVertex.compareTo(otherArc.getToVertex());
        if (result != 0) {
            return result;
        }
        result = Float.compare(pathLength, otherArc.getPathLength());
        if (result != 0) {
            return result;
        }
        result = MyVector3f.compare(startDirection,
                otherArc.getStartDirection());
        /*
         * Verify consistency with equals().
         */
        if (result == 0) {
            assert this.equals(otherArc);
        }
        return result;
    }
    // *************************************************************************
    // Object methods

    /**
     * Compare for equality.
     *
     * @param otherObject (unaffected)
     * @return true if the arcs are equivalent, otherwise false
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        } else if (otherObject instanceof NavArc) {
            NavArc otherArc = (NavArc) otherObject;
            if (!fromVertex.equals(otherArc.getFromVertex())) {
                return false;
            } else if (!toVertex.equals(otherArc.getToVertex())) {
                return false;
            } else if (pathLength != otherArc.getPathLength()) {
                return false;
            } else {
                boolean result =
                        startDirection.equals(otherArc.getStartDirection());
                return result;
            }
        }
        return false;
    }

    /**
     * Generate the hash code for this arc.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Float.floatToIntBits(this.pathLength);
        hash = 17 * hash + Objects.hashCode(this.fromVertex);
        hash = 17 * hash + Objects.hashCode(this.toVertex);
        hash = 17 * hash + Objects.hashCode(this.startDirection);

        return hash;
    }

    /**
     * Format this arc as a text string.
     *
     * @return description (not null)
     */
    @Override
    public String toString() {
        String fromString = fromVertex.toString();
        String toString = toVertex.toString();
        String dirString = startDirection.toString();
        String result = String.format("%s to %s len=%f dir=%s",
                fromString, toString, pathLength, dirString);

        return result;
    }
}