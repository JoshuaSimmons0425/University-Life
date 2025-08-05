/**
 * abstractdb.cpp
 * C++ source file that should contain implementation for member functions
 * - loadCSV()
 * - saveCSV()
 * 
 * You need to modify this file to implement the above-mentioned member functions
 * as specified in the hand-out (Tasks 4 and 5)
 */ 

#include "abstractdb.hpp"
#include <fstream>
#include <iostream>
#include <sstream>
#include <cctype>
#include <cstring>
#include <string>

namespace nwen {

// Saves all movie records currently in the database table to a CSV file.

bool AbstractDbTable::saveCSV(const std::string& filename){

    std::ofstream file(filename, std::ios::trunc); // Truncate file if it exists
    if(!file.is_open()){
        std::cerr << "Error opening file" << filename << std::endl;
        return false;
    }

    for (int i = 0; i < this->rows(); ++i){
        const movie* m = this->get(i);
        if(m == nullptr){
            file.close();
            return false; // Invalid movie entry

        }
        // Write movie data in CSV format with quotes around title and director
        file << m->id << ",\"" << m->title << "\"," << m->year << ",\"" << m->director << "\"" <<"\n";
    }
    if(file.fail()){
        file.close();
        return false; // Write failure
    }

    file.close();
    return true; // Successfully saved
}

//Loads movie records from a CSV file into the database table.

bool AbstractDbTable::loadCSV(const std::string& filename){
    std::ifstream file(filename);
    if(!file.is_open()){
        std::cerr << "Error opening file" << filename << std::endl;
        return false;
    }

std::string line;
    while (std::getline(file, line)) {
        std::istringstream iss(line);
        movie m;
        std::string id_str, title, year_str, director;

        // Read plain, unquoted CSV fields (id,title,year,director)
        if (!std::getline(iss, id_str, ',') ||
            !std::getline(iss, title, ',') ||
            !std::getline(iss, year_str, ',') ||
            !std::getline(iss, director)) {
            std::cerr << "Invalid line format: " << line << std::endl;
            file.close();
            return false;
        }

        try {
            m.id = std::stoul(id_str);
            m.year = static_cast<unsigned short>(std::stoul(year_str));
        } catch (...) {
            std::cerr << "Conversion error on line: " << line << std::endl;
            file.close();
            return false;
        }

        // Copy string values to char arrays
        std::strncpy(m.title, title.c_str(), sizeof(m.title) - 1);
        m.title[sizeof(m.title) - 1] = '\0';

        std::strncpy(m.director, director.c_str(), sizeof(m.director) - 1);
        m.director[sizeof(m.director) - 1] = '\0';

        // Add to table; fail on duplicate or invalid entry
        if (!this->add(m)) {
            std::cerr << "Failed to add movie: (" << m.id << ", " << m.title << ", " 
                      << m.year << ", " << m.director << ")" << std::endl;
            file.close();
            return false;
        }
    }

    file.close();
    return true;
}
}
