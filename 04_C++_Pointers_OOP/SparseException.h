// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others

#include "iostream"
using namespace std;

class SparseException
{
	public:
		SparseException();
		SparseException(char*);
		void printMessage()const;
	private:
		const char *message;
};
