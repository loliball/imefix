package loli.ball.imefix

import com.intellij.application.subscribe
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.impl.MessageBusImpl
import com.intellij.util.messages.impl.RootBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.AWTEvent
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import java.io.File
import java.util.*


class MAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

    }

}

class IMEFix : AWTEventListener, AnActionListener, Disposable {

    init {
        println("loli.ball.demoplugin loaded")
        System.load("D:\\CLionProjects\\imefix\\cmake-build-release\\imefix.dll")
    }

    fun loadJarDll(name: String) {
        val temp = File.createTempFile(name, "")
        IMEFix::class.java.getResourceAsStream(name)!!.use { inp ->
            temp.outputStream().use { out ->
                inp.copyTo(out)
            }
        }
        System.load(temp.getAbsolutePath())
    }

    fun init() {
        println("loli.ball.demoplugin loaded")
        AnActionListener.TOPIC.subscribe(this, this)
        val eventMask = AWTEvent.KEY_EVENT_MASK
//            AWTEvent.MOUSE_EVENT_MASK or
//                    AWTEvent.MOUSE_MOTION_EVENT_MASK or
//                    AWTEvent.WINDOW_EVENT_MASK or
//                    AWTEvent.WINDOW_STATE_EVENT_MASK
        val toolkit = Toolkit.getDefaultToolkit()
        toolkit.addAWTEventListener(this, eventMask)
        val app = ApplicationManager.getApplication()
        val root = ApplicationManager.getApplication().messageBus as RootBus
        val ff = MessageBusImpl::class.java.getDeclaredField("subscribers")
        ff.isAccessible = true
        val queue = ff.get(root) as Queue<*>
        queue.forEach {
            println(it)
        }
//        val am = ApplicationManager.getApplication().getServiceIfCreated(ActionManager::class.java)
//        val actionIds = (am as ActionManagerImpl).actionIds
//        println(actionIds)
        val ctx = CoroutineScope(Dispatchers.Main)
        ctx.launch {
            delay(3000)
            val am = ApplicationManager.getApplication().getServiceIfCreated(ActionManager::class.java)
            val actionIds = (am as ActionManagerImpl).getAction("ShowSettings")!!
            val app = ApplicationManager.getApplication()
            // main thread only!
            ActionUtil.invokeAction(actionIds, DataContext.EMPTY_CONTEXT, ActionPlaces.EDITOR_INLAY, null, null)
        }
        // (ApplicationManager.getApplication().messageBus as RootBus).subscribers_field
    }

    override fun beforeShortcutTriggered(shortcut: Shortcut, actions: MutableList<AnAction>, dataContext: DataContext) {
        println("beforeShortcutTriggered $shortcut $actions $dataContext")
//        super.beforeShortcutTriggered(shortcut, actions, dataContext)
    }

    override fun beforeActionPerformed(action: AnAction, event: AnActionEvent) {
        println("action $action event $event")
//        val toolkit = Toolkit.getDefaultToolkit()
//        toolkit.awtEventListeners.forEach {
//            val proxy = it as AWTEventListenerProxy
//            proxy.listener
//            println("el: $it")
//        }
        super.beforeActionPerformed(action, event)
    }

    override fun eventDispatched(event: AWTEvent?) {
        println("dispatch event $event")
    }

    override fun dispose() {
        println("dispose")
    }


}