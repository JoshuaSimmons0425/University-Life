/**
 * abstractdb.hpp
 * C++ header file that should contain declaration for
 * - struct movie (given)
 * - AbstractDbTable abstract class
 * 
 * You need to modify this file to declare AbstractDbTable abstract class 
 * as specified in the hand-out (Task 1)
 */ 



#ifndef __ABSTRACT_DB_HPP__
#define __ABSTRACT_DB_HPP__

#include <string>

namespace nwen
{
    struct movie {
        unsigned long id;
        char title[50];
        unsigned short year;
        char director[50];
    };
    class AbstractDbTable{
    public:
        int* data; // Placeholder member
        virtual int rows() const = 0; // Returns the number of rows (records) in the table
        virtual const movie* get(int i) const = 0; // Returns a pointer to the movie at index i, or nullptr if index is invalid
        virtual bool add(const movie& m) = 0;
        virtual bool update(unsigned long i, const movie& m) = 0;  // Adds a movie record to the table; returns false on failure (e.g. duplicate ID)
        virtual bool remove(unsigned long i) = 0;   // Updates the record with the given ID; returns false if no matching ID exists
        bool loadCSV(const std::string& filename);   // Loads movie records from a CSV file; implemented in abstractdb.cpp
        bool saveCSV(const std::string& filename);  // Saves movie records to a CSV file; implemented in abstractdb.cpp
        virtual ~AbstractDbTable(){  // Virtual destructor to ensure proper cleanup in derived classes
            delete data;
        }
    };
}

#endif /* __ABSTRACT_DB_HPP__ */
