/**
 * vectordb.hpp
 * C++ header file that should contain declaration for
 * - VectorDbTable class
 * 
 * You need to modify this file to declare VectorDbTable class 
 * as specified in the hand-out (Task 2)
 */ 

#include "abstractdb.hpp"
#include <vector>

namespace nwen {

class VectorDbTable : public AbstractDbTable {
private:
    std::vector<movie> movies; // Container to store the movie records

    int findIndexBy(unsigned long id) const; // Declaration of a helper method to find index of vector table
public:
    int* data; // Placeholder member

    int rows() const override; // Returns the number of records currently in the table.
    const movie* get(int i) const override; // Retrieves a pointer to the movie at index i
    bool add(const movie& m) override; // Adds a new movie to the table.
    bool update(unsigned long i, const movie& m) override; // Updates the movie record with the given ID.
    bool remove(unsigned long i) override; // Removes the movie record with the given ID.
};
}
