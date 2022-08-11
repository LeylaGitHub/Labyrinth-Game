package cga.exercise.components.geometry

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

open class Transformable(private var modelMatrix: Matrix4f = Matrix4f(), var parent: Transformable? = null) {
    /**
     * Returns copy of object model matrix
     * @return modelMatrix
     */
    fun getModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
        //throw NotImplementedError()
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    fun getWorldModelMatrix(): Matrix4f {
        val mult: Matrix4f
        //if not scene parent node, look for parent
        if(parent != null){
            // recursive multiplication of parents' matrices
            // return parent Model Matrix * current Model Matrix
            mult = parent?.getWorldModelMatrix()?.mul(modelMatrix)!!

            return mult
        }
        return getModelMatrix()

        //throw NotImplementedError()
    }

    /**
     * Rotates object around its own origin.
     * @param pitch DEGREE angle around x-axis ccw
     * @param yaw DEGREE angle around y-axis ccw
     * @param roll DEGREE angle around z-axis ccw
     */
    fun rotate(pitch: Float, yaw: Float, roll: Float) {

        if (pitch != 0f) modelMatrix.rotate(Math.toRadians(pitch), 1f,0f,0f)
        if (yaw != 0f) modelMatrix.rotate(Math.toRadians(yaw) , 0f,1f,0f)
        if (roll != 0f) modelMatrix.rotate(Math.toRadians(roll), 0f,0f,1f)

        //throw NotImplementedError()
    }

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {
//        // Translate to the rotation center.
//        modelMatrix.translate(altMidpoint);
//
//        // Rotate
//        if (pitch != 0f) modelMatrix.rotate((pitch * Math.PI / 180).toFloat(), 1f,0f,0f)
//        if (yaw != 0f) modelMatrix.rotate((yaw * Math.PI / 180).toFloat(), 0f,1f,0f)
//        if (roll != 0f) modelMatrix.rotate((roll * Math.PI / 180).toFloat(), 0f,0f,1f)
//
//        // Translate back
//        modelMatrix.translate(Vector3f(altMidpoint.x * -1, altMidpoint.y * -1, altMidpoint.z * -1));

        val vec = Vector3f(altMidpoint)

        val transposeMatrix = Matrix4f().translate(vec).rotateXYZ(Math.toRadians(pitch), Math.toRadians(yaw), Math.toRadians(roll))
            .translate(vec.negate())
        modelMatrix = transposeMatrix.mul(modelMatrix)

        //throw NotImplementedError()
    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    fun translate(deltaPos: Vector3f) {
        modelMatrix.translate(deltaPos)

        //throw NotImplementedError()
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: this operation has to be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    fun preTranslate(deltaPos: Vector3f) {
        modelMatrix = Matrix4f().translate(deltaPos).mul(modelMatrix)
//        modelMatrix.translateLocal(deltaPos)  //vermeiden translateLocal zu verwenden
//        throw NotImplementedError()
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    fun scale(scale: Vector3f) {
        modelMatrix.scale(scale)
        //throw NotImplementedError()
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    fun getPosition(): Vector3f {
        val col = Vector3f() //to store last column
        return getModelMatrix().getColumn(3, col)
        //throw NotImplementedError()
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    fun getWorldPosition(): Vector3f {
        val col = Vector3f() //to store last column
        return getWorldModelMatrix().getColumn(3, col)
//        throw NotImplementedError()
    }

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    fun getXAxis(): Vector3f {
        val col = Vector3f() //to store last column
        getModelMatrix().getColumn(0, col)
        return col.normalize()
        //throw NotImplementedError()
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    fun getYAxis(): Vector3f {
        val col = Vector3f() //to store last column
        getModelMatrix().getColumn(1, col)
        return col.normalize()
        //throw NotImplementedError()
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    fun getZAxis(): Vector3f {
        val col = Vector3f() //to store last column
        getModelMatrix().getColumn(2, col)
        return col.normalize()
        //throw NotImplementedError()
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    fun getWorldXAxis(): Vector3f {
        val col = Vector3f() //to store last column
        getWorldModelMatrix().getColumn(0, col)
        return col.normalize()
        //throw NotImplementedError()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    fun getWorldYAxis(): Vector3f {
        val col = Vector3f() //to store last column
        getWorldModelMatrix().getColumn(1, col)
        return col.normalize()
        //throw NotImplementedError()
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    fun getWorldZAxis(): Vector3f {
        val col = Vector3f() //to store last column
        getWorldModelMatrix().getColumn(2, col)
        return col.normalize()
        //throw NotImplementedError()
    }
}