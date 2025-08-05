/**
 * vectordb.cpp
 * C++ source file that should contain implementation for member functions
 * - rows()
 * - get()
 * - add()
 * - update()
 * - remove()
 *
 * You need to modify this file to implement the above-mentioned member functions
 * as specified in the hand-out (Task 3)
 */

#include "vectordb.hpp"

// Method to return the number of rows of the vector table
int nwen::VectorDbTable::rows() const {
    return static_cast<int>(movies.size());
}

// Method to return a pointer to a movie we searching through the vector table
const nwen::movie* nwen::VectorDbTable::get(int i) const{
    if(i >= 0 && static_cast<size_t>(i) < movies.size()) {
        return &movies[i]; // Return the movie that's pointed to within the index
    }
    return nullptr; // If the index is empty, return a null pointer
}

// Helper method to find the movie id via index
int nwen::VectorDbTable::findIndexBy(unsigned long id) const {
    for (size_t i = 0; i < movies.size(); i++) {
        if (movies[i].id == id) {
            return static_cast<int>(i);
        }
    }
    return -1;
}

bool nwen::VectorDbTable::add(const movie& m) {
    if (findIndexBy(m.id) == -1) {
        movies.push_back(m);
        return true;
    }
    return false;
}

bool nwen::VectorDbTable::update(unsigned long id, const movie& m) {
    int index = findIndexBy(id);
    if (index != -1) {
        movies[index] = m;
        return true;
    }
    return false;
}

bool nwen::VectorDbTable::remove(unsigned long id) {
    int index = findIndexBy(id);
    if (index != -1) {
        movies.erase(movies.begin() + index);
        return true;
    }
    return false;
}


