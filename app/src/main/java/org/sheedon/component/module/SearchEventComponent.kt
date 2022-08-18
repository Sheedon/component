package org.sheedon.component.module

import org.sheedon.binging.AbstractEventComponent
import org.sheedon.binging.AbstractEventConverter
import org.sheedon.binging.EventHandler
import org.sheedon.component.module.scan.ScanConverter
import org.sheedon.component.module.scan.ScanHandler
import org.sheedon.component.module.search.SearchConverter
import org.sheedon.component.module.search.SearchDataHandler
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/16 22:37
 */
class SearchEventComponent : AbstractEventComponent<SearchCallback, SearchHandler>() {


    override fun loadEventArray(): ArrayList<AbstractEventConverter<out EventHandler>> {
        return arrayListOf(ScanConverter(), SearchConverter())
    }

    override fun createRealHandler(handlerArray: Array<out EventHandler>): SearchHandler {
        return object : SearchHandler {

            private val handlerMap = HashMap<String, EventHandler>()

            init {
                for (handler in handlerArray) {
                    if (handler is ScanHandler) {
                        ScanHandler::class.java.canonicalName?.let { handlerMap.put(it, handler) }
                    } else if (handler is SearchDataHandler) {
                        SearchDataHandler::class.java.canonicalName?.let {
                            handlerMap.put(
                                it,
                                handler
                            )
                        }
                    }
                }
            }

            override fun scan() {
                val eventHandler = handlerMap[ScanHandler::class.java.canonicalName ?: ""]
                (eventHandler as? ScanHandler)?.scan()
            }

            override fun search(searchDate: String) {
                val eventHandler = handlerMap[SearchDataHandler::class.java.canonicalName ?: ""]
                (eventHandler as? SearchDataHandler)?.search(searchDate)
            }

        }
    }
}