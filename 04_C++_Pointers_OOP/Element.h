// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others
#include <iostream>
#include <ctime>
#include <cctype>
#include <iomanip>
#include <cmath>
#include <cstdlib>
using namespace std;

const int MAXROW = 10;
const int MAXCOL = 10;

class Element
{
	public:
		Element();
		Element(int,int,int);
		Element(const Element&);
		~Element();

		// assessor functions
		int getRow()const;
		int getCol()const;
		int getValue()const;

		// mutator functions
		void set(int,int);
		void setValue(int);

		// print function
		void printElement()const;

		// other useful functions
		bool isEqual(const Element)const;
		bool isSmaller(const Element)const;

	private:
		int row;
		int col;
		int value;
};

