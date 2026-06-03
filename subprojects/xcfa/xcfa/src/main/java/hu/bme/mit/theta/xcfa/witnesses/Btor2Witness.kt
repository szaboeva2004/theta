/*
 *  Copyright 2026 Budapest University of Technology and Economics
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package hu.bme.mit.theta.xcfa.witnesses

import java.io.File
import java.io.FileOutputStream
import kotlin.text.get

class Btor2Witness(val maxFrameIndex: Int) {
  // Key is the name of the input e.g. input_32, the list is all of the values it had
  private val inputList = mutableMapOf<Int, ArrayList<String>>()
  fun addInputList(currentInputs: List<Pair<Int, String>>) {
    currentInputs.forEach { (index, value) ->
      addInput(index, value)
    }
  }

  fun addInput(index: Int, value: String) {
    if (!inputList.containsKey(index)) {
      inputList[index] = ArrayList()
    }
    inputList[index]!!.add(value)
  }

  fun addEmptyFrame() {
      inputList.values.forEach { list ->
        if (list.isNotEmpty()) {
          list.add(list.last())
        }
      }
    }

  fun serialize(witnessFile: File) {
    FileOutputStream(witnessFile, true).bufferedWriter().use { out ->
      out.appendLine("#0")
      for (frameIndex in 0 until maxFrameIndex) {
        out.appendLine("@$frameIndex")
        var iter = 0
        inputList.forEach { (_, values) ->
          out.appendLine("$iter ${values.get(frameIndex)}")
          ++iter
        }
      }
      out.appendLine(".")
    }
  }

}