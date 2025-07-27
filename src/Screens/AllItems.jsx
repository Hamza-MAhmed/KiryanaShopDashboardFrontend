import { StyleSheet, Text, View, FlatList } from 'react-native'
import React from 'react'
// import { FlatList } from 'react-native/types_generated/index'

const AllItems = ({data}) => {
  return (
    <View>
      <View style={styles.headingContainer}>
        <Text style={styles.headingText}>Items</Text>
        <Text style={styles.headingText}>Quantity</Text>
      </View>
      
      <FlatList
        data={data}
        keyExtractor={(item) => item.id}
        renderItem={({item}) => (
          <View style={[styles.itemContainer, {backgroundColor:item.stock<20 ? "#FFCCCC" : "#07F66FFF"}]}>
            <Text style={styles.itemText}>{item.name}</Text>
            <Text style={styles.itemText}>{item.stock}</Text>
          </View>
        )}
        
        contentContainerStyle={{gap:10}}
        />

    </View>
  )
}

export default AllItems

const styles = StyleSheet.create({
  headingContainer: {
    paddingVertical:10,
    flexDirection: "row",
    justifyContent: "space-between",
    paddingHorizontal:15
  },
  headingText: {
    fontWeight:"500",
    fontSize:16
  },
  itemContainer:{
    flexDirection:"row",
    justifyContent:"space-between",
    paddingHorizontal:15,
    paddingVertical:10,
    borderRadius:8
  },
  itemText: {
    fontWeight:"400",
    fontSize:15
  },
})