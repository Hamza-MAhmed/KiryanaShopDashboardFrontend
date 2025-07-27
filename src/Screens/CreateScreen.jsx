import { StyleSheet, Text, View, TextInput, Pressable, FlatList } from 'react-native'
import React, { useState } from 'react'
// import { TextInput } from 'react-native/types_generated/index'

const CreateScreen = ({ data, setdata }) => {

  const [itemName, setItemName] = useState('')
  const [stockAmt, setStockAmt] = useState('')
  const [isEdit, setisEdit] = useState(false)
  const [editItemId, seteditItemId] = useState(null)

  const addItemHandler = () => {
    const newItem = {
      id: Date.now(),
      name: itemName,
      stock: stockAmt
    }

    setdata([...data, newItem])
    setItemName('')
    setStockAmt('')
    setisEdit(false)
  }

  const editItemHandler = (item) => {
    setisEdit(true)
    setItemName(item.name)
    seteditItemId(item.id)
  }

  const updateItemHandler = () => {
    setdata(data.map((item) => {
      return item.id === editItemId ? { ...item, name: itemName, stock: stockAmt } : item
    }))
    setItemName('');
    setStockAmt('');
    setisEdit(false);
  }

  const deleteItemHandler = (id) => {
    setdata(data.filter((item) => item.id !== id))
  }
  return (
    <View style={styles.container}>
      <TextInput
        placeholder="Enter an item name..."
        placeholderTextColor="#999"
        style={styles.input}
        value={itemName}
        onChangeText={(item) => setItemName(item)}
      />
      <TextInput
        placeholder="Enter stock amount..."
        placeholderTextColor="#999"
        keyboardType='numeric'
        style={styles.input}
        value={stockAmt}
        onChangeText={(item) => {
          let numericValue = item.replace(/[^0-9]/g, '');

          // Prevent 0 as the first character (and negative numbers automatically)
          if (numericValue.startsWith('0')) {
            numericValue = numericValue.replace(/^0+/, '');
          }

          setStockAmt(numericValue);
        }
        }
      />

      <Pressable style={styles.addButton} onPress={() => isEdit ? updateItemHandler() : addItemHandler()}>
        <Text style={styles.btnText}>{isEdit ? "Edit item in stock" : "Add item in stock"}</Text>
      </Pressable>

      <View style={{ marginTop: 10 }}>
        <Text style={styles.headingText}>All items in the stock</Text>

        <FlatList
          data={data}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <View style={[styles.itemContainer, { backgroundColor: item.stock < 20 ? "#FFCCCC" : "#07F66FFF" }]}>
              <Text style={styles.itemText}>{item.name}</Text>
              <View style={{ flexDirection: "row", gap: 20 }}>
                <Text style={styles.itemText}>{item.stock}</Text>

                <Pressable onPress={() => editItemHandler(item)}>
                  <Text style={[styles.itemText, {borderWidth:2, borderColor:"black", padding:2, borderRadius:5}]}>Edit</Text>
                </Pressable>
                <Pressable onPress={() => deleteItemHandler(item.id)}>
                  <Text style={[styles.itemText, {borderWidth:2, borderColor:"black", padding:2, borderRadius:5}]}>Delete</Text>
                </Pressable>
              </View>
            </View>
          )}

          contentContainerStyle={{ gap: 10 }}
        />

      </View>

    </View>
  )
}

export default CreateScreen

const styles = StyleSheet.create({
  container: {
    paddingVertical: "4%",
    gap: 10
  },

  input: {
    borderWidth: 2,
    borderColor: "#D7F6BFFF",
    paddingHorizontal: 15,
    paddingVertical: 10,
    borderRadius: 7
  },
  addButton: {
    backgroundColor: "#CABFEEFF",
    paddingHorizontal: 15,
    paddingVertical: 10,
    borderRadius: 7,
    justifyContent: "center",
    alignItems: "center"
  },
  btnText: {
    color: "white",
    fontWeight: "bold",
    fontSize: 15
  },
  headingContainer: {
    paddingVertical: 10,
    flexDirection: "row",
    justifyContent: "space-between",
    paddingHorizontal: 15
  },
  headingText: {
    fontWeight: "500",
    fontSize: 16,
    marginBottom: 15
  },
  itemContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingHorizontal: 15,
    paddingVertical: 10,
    borderRadius: 8
  },
  itemText: {
    fontWeight: "400",
    fontSize: 15
  },
})