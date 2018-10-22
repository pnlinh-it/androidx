/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.ui.painting.alignment

import androidx.ui.lerpDouble
import androidx.ui.engine.text.TextDirection

/**
 * Base class for [Alignment] that allows for text-direction aware
 * resolution.
 *
 * A property or argument of this type accepts classes created either with [new
 * Alignment] and its variants, or [new AlignmentDirectional].
 *
 * To convert a [AlignmentGeometry] object of indeterminate type into a
 * [Alignment] object, call the [resolve] method.
 */
abstract class AlignmentGeometry {

    abstract val _x: Double

    abstract val _start: Double

    abstract val _y: Double

    /**
     * Returns the sum of two [AlignmentGeometry] objects.
     *
     * If you know you are adding two [Alignment] or two [AlignmentDirectional]
     * objects, consider using the `+` operator instead, which always returns an
     * object of the same type as the operands, and is typed accordingly.
     *
     * If [add] is applied to two objects of the same type ([Alignment] or
     * [AlignmentDirectional]), an object of that type will be returned (though
     * this is not reflected in the type system). Otherwise, an object
     * representing a combination of both is returned. That object can be turned
     * into a concrete [Alignment] using [resolve].
     */
    open fun add(other: AlignmentGeometry): AlignmentGeometry {
        return _MixedAlignment(
                _x + other._x,
                _start + other._start,
                _y + other._y
        )
    }

    /**
     * Returns the negation of the given [AlignmentGeometry] object.
     *
     * This is the same as multiplying the object by -1.0.
     *
     * This operator returns an object of the same type as the operand.
     */
    abstract operator fun unaryMinus(): AlignmentGeometry

    /**
     * Scales the [AlignmentGeometry] object in each dimension by the given factor.
     *
     * This operator returns an object of the same type as the operand.
     */
    abstract operator fun times(other: Double): AlignmentGeometry

    /**
     * Divides the [AlignmentGeometry] object in each dimension by the given factor.
     *
     * This operator returns an object of the same type as the operand.
     */
    abstract operator fun div(other: Double): AlignmentGeometry

    /**
     * Integer divides the [AlignmentGeometry] object in each dimension by the given factor.
     *
     * This operator returns an object of the same type as the operand.
     */
    abstract fun truncDiv(other: Double): AlignmentGeometry

    /**
     * Computes the remainder in each dimension by the given factor.
     *
     * This operator returns an object of the same type as the operand.
     */
    abstract operator fun rem(other: Double): AlignmentGeometry

    companion object {
        /**
         * Linearly interpolate between two [AlignmentGeometry] objects.
         *
         * If either is null, this function interpolates from [Alignment.center], and
         * the result is an object of the same type as the non-null argument.
         *
         * If [lerp] is applied to two objects of the same type ([Alignment] or
         * [AlignmentDirectional]), an object of that type will be returned (though
         * this is not reflected in the type system). Otherwise, an object
         * representing a combination of both is returned. That object can be turned
         * into a concrete [Alignment] using [resolve].
         *
         * The `t` argument represents position on the timeline, with 0.0 meaning
         * that the interpolation has not started, returning `a` (or something
         * equivalent to `a`), 1.0 meaning that the interpolation has finished,
         * returning `b` (or something equivalent to `b`), and values in between
         * meaning that the interpolation is at the relevant point on the timeline
         * between `a` and `b`. The interpolation can be extrapolated beyond 0.0 and
         * 1.0, so negative values and values greater than 1.0 are valid (and can
         * easily be generated by curves such as [Curves.elasticInOut]).
         *
         * Values for `t` are usually obtained from an [Animation<double>], such as
         * an [AnimationController].
         */
        fun lerp(a: AlignmentGeometry?, b: AlignmentGeometry?, t: Double): AlignmentGeometry? {
            if (a == null && b == null)
                return null
            if (a == null)
                return b !! * t
            if (b == null)
                return a * (1.0 - t)
            if (a is Alignment && b is Alignment)
                return Alignment.lerp(a, b, t)
            if (a is AlignmentDirectional && b is AlignmentDirectional)
                return AlignmentDirectional.lerp(a, b, t)
            return _MixedAlignment(
                    lerpDouble(a._x, b._x, t),
                    lerpDouble(a._start, b._start, t),
                    lerpDouble(a._y, b._y, t)
            )
        }
    }

    /**
     * Convert this instance into a [Alignment], which uses literal
     * coordinates (the `x` coordinate being explicitly a distance from the
     * left).
     *
     * See also:
     *
     *  * [Alignment], for which this is a no-op (returns itself).
     *  * [AlignmentDirectional], which flips the horizontal direction
     *    based on the `direction` argument.
     */
    abstract fun resolve(direction: TextDirection?): Alignment

    override fun toString(): String {
        if (_start == 0.0)
            return Alignment._stringify(_x, _y)
        if (_x == 0.0)
            return AlignmentDirectional._stringify(_start, _y)
        return Alignment._stringify(_x, _y) + " + " + AlignmentDirectional._stringify(_start, 0.0)
    }

    // Auto-generated by Android Studio
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AlignmentGeometry) return false

        if (_x != other._x) return false
        if (_start != other._start) return false
        if (_y != other._y) return false

        return true
    }

    // Auto-generated by Android Studio
    override fun hashCode(): Int {
        var result = _x.hashCode()
        result = 31 * result + _start.hashCode()
        result = 31 * result + _y.hashCode()
        return result
    }
}
