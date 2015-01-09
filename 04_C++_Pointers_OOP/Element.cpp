// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others

#include "Element.h"

Element::Element()
{
	row = 0;
	col = 0;
}
Element::Element(int rows,int cols,int values)
{
	this->row = rows;
	this->col = cols;
	this->value = values;
}
Element::Element(const Element& e)
{
	this->row = e.row;
	this->col = e.col;
	this->value = e.value;
}
Element::~Element()
{

}


int Element::getRow()const
{
	return row;
}
int Element::getCol()const
{
	return col;
}
int Element::getValue()const
{
	return value;
}


void Element::set(int rows,int cols)
{
	(*this).row = rows;
	(*this).col = cols;
}
void Element::setValue(int values)
{
	this->value = values;
}


void Element::printElement()const
{
	cout << "("  << row
		 << ", " << col
		 << ", " << value
		 << ")"	;
}


bool Element::isEqual(const Element e)const
{
	if((this->row==e.row)&&(this->col==e.col))
		return true;
	else
		return false;
}
bool Element::isSmaller(const Element e)const
{
	if (this->row<e.row)
		return true;
	else if ((this->row==e.row)&&(this->col<e.col))
		return true;
	else
		return false;
}

