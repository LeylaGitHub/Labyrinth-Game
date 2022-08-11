package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import org.joml.Math
import org.joml.Math.sin
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12

class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    private val meshBoden: Mesh

    val bodenMatrix = Matrix4f()

    private val camera: TronCamera

    private val bodenRen: Renderable

    private var diff: Texture2D
    private var emit: Texture2D
    private var spec: Texture2D

//    private var pointLight: PointLight
    private var spotLight: SpotLight
//    private var lightPoints: MutableList<PointLight> = arrayListOf()


    //scene setup
    init {
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
        //initial opengl state
        // color of scene
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLError.checkThrow()

        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        //Uhrzeigersinn gebildeten Dreiecken werden nicht gerendert
        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW)
        glCullFace(GL_BACK)

        //---------------------------------------------
        // obj loader

        //---- FLOOR ----
        val resBoden = OBJLoader.loadOBJ("assets/models/ground.obj") // boden
        val objBoden = resBoden.objects[0].meshes[0]
        val vertexDataBoden = objBoden.vertexData
        val indexDataBoden = objBoden.indexData

        val posBoden = VertexAttribute(3, GL_FLOAT, 8 * 4, 0)
        val texBoden = VertexAttribute(2, GL_FLOAT, 8 * 4, 3 * 4)
        val norBoden = VertexAttribute(3, GL_FLOAT, 8 * 4, 5 * 4)
        val vertexAttributeArrayBoden = arrayOf<VertexAttribute>(posBoden, texBoden, norBoden)

        // LOAD AND SET EACH TEXTURE PARAM
        //params: wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int
        diff = Texture2D.invoke("assets/textures/ground_diff.png", false)                       // Dateien für Bodentexturen ändern
        diff.setTexParams(GL12.GL_REPEAT, GL12.GL_REPEAT, GL12.GL_LINEAR, GL12.GL_LINEAR)

        emit = Texture2D.invoke("assets/textures/ground_emit.png", true)
        emit.setTexParams(GL12.GL_REPEAT, GL12.GL_REPEAT, GL12.GL_LINEAR_MIPMAP_LINEAR, GL12.GL_LINEAR)

        spec = Texture2D.invoke("assets/textures/ground_spec.png", false)
        spec.setTexParams(GL12.GL_REPEAT, GL12.GL_REPEAT, GL12.GL_LINEAR, GL12.GL_LINEAR)

        val matBoden = Material(
            diff,
            emit,
            spec,
            60f,
            Vector2f(64.0f, 64.0f)
        )

        // USE ALL OUR DATA TO GENERATE A FINAL MESH
        meshBoden = Mesh(vertexDataBoden, indexDataBoden, vertexAttributeArrayBoden, matBoden)

        // USE MESH AND FLOOR IDENTITY MATRIX TO GENERATE A RENDERABLE OBJ FOR OUR SCENE
        bodenRen = Renderable(mutableListOf(meshBoden), bodenMatrix, null)

        //---- CAMERA ----
        camera = TronCamera()
        camera.rotate(-35f, 0f, 0f)
        camera.translate(Vector3f(0f, 1f, 4.0f))

//        // pointLight //
//        pointLight = PointLight(Vector3f(0f, 1f, 0f), Vector3f(1f, 0f, 1f)) //Position z value? (??)
//        lightPoints.add(pointLight)

        // spotLight //
        spotLight = SpotLight(
            Vector3f(0f, 1f, 0f),
            Vector3f(1f, 1f, 1f),
            Math.toRadians(3f),
            Math.toRadians(5f),
            Vector3f(0f, 1f, 0f)
        )

//        // more pointlights //
//        lightPoints.add(PointLight(Vector3f(64f, 1f, 64f), Vector3f(0.5f, 1f, 0.5f)))
//        lightPoints.add(PointLight(Vector3f(64f, 1f, -64f), Vector3f(0f, 1f, 0f)))
//        lightPoints.add(PointLight(Vector3f(-64f, 1f, -64f), Vector3f(1f, 0.5f, 1f)))
//        lightPoints.add(PointLight(Vector3f(-64f, 1f, 64f), Vector3f(1f, 1f, 1f)))

        staticShader.use()
    }

    fun render(dt: Float, t: Float) {
        staticShader.setUniform("color_ground", Vector3f(0.0f, 1.0f, 0.0f))

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.bind(staticShader)
//        lightPoints.forEach { light ->
//            light.bind(staticShader)
//        }
//        pointLight.bind(staticShader)
        spotLight.bind(staticShader)

        // FINALLY RENDER OUR MESH AND LOAD UNIFORMS ONTO SHADER
        bodenRen.render(staticShader)
        staticShader.setUniform("color_ground", Vector3f( //Farbveränderung 4.5
            sin(window.currentTime + 90 % 360),
            sin(window.currentTime + 180 % 360),
            sin(window.currentTime + 270 % 360)
        ))

    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW.GLFW_KEY_W)) {
            motorrad?.translate(Vector3f(0f, 0f, -dt * 10))
            if (window.getKeyState(GLFW.GLFW_KEY_A))
                motorrad?.rotate(0f, dt * 40, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_D))
                motorrad?.rotate(0f, -dt * 40, 0f)

        }
        if (window.getKeyState(GLFW.GLFW_KEY_S)) {
            motorrad?.translate(Vector3f(0f, 0f, dt * 10))
            if (window.getKeyState(GLFW.GLFW_KEY_A))
                motorrad?.rotate(0f, -dt * 100, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_D))
                motorrad?.rotate(0f, dt * 100, 0f)
        }

        if (window.getKeyState(GLFW.GLFW_KEY_A)) {
            motorrad?.rotate(0f, dt * 40, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_W))
                motorrad?.rotate(0f, dt * 40, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_S))
                motorrad?.rotate(0f, -dt * 40, 0f)
        }
        if (window.getKeyState(GLFW.GLFW_KEY_D)) {
            motorrad?.rotate(0f, -dt * 40, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_W))
                motorrad?.rotate(0f, -dt * 40, 0f)
            if (window.getKeyState(GLFW.GLFW_KEY_S))
                motorrad?.rotate(0f, dt * 40, 0f)
        }

    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    var x: Double = window.windowWidth / 2.0

    fun onMouseMove(xpos: Double, ypos: Double) {
        val xMovement = xpos.minus(x).toFloat() // difference between old & new mouse pos
        println(x)
        camera.rotateAroundPoint(0f, xMovement * -0.007f, 0f, /*Textur der Figur*/!!.getWorldPosition())
        x = xpos
    }

    fun cleanup() {
        meshBoden.cleanup()
    }
}
