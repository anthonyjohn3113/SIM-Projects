// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others

#include "SparseException.h"

SparseException::SparseException()
{
	message = " ";
}
SparseException::SparseException(char* messages)
{
	this->message = messages;
}
void SparseException::printMessage()const
{
	cout << "Sparse exception caught: " << message;
}

