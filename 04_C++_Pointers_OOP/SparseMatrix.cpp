// Name: Garcia Anthony John Abril
// Seat : 14
// Own work, code not shared
// class examples were used as reference
// thus may have similarities with others

#include "SparseMatrix.h"

SparseMatrix::SparseMatrix()
{
	head = NULL;
}
SparseMatrix::~SparseMatrix()
{
	NodePtr temp;
	static int i = 1;
	cout << "\nGarbage collection - Matrix ";
	cout << i << endl;
	while (head != NULL)
	{
		temp = head;
		head = head -> next;

		cout << "Element ";
		temp->e.printElement();
		delete temp;
		cout << " deleted\n";
	}
	i++;
	cout << endl
		 << setw(60) << setfill('=')
		 << "="      << setfill(' ')
		 << endl;
}
SparseMatrix::SparseMatrix(const SparseMatrix& matrix)
{
	NodePtr p1 = NULL;
	NodePtr p2 = NULL;
		
	head = new Node;
	head->e = matrix.head->e;

	p1 = head;
	p2 = matrix.head->next;

	while(p2)
	{
		p1->next = new Node;
		p1 = p1->next;
		p1->e = p2->e;

		p2 = p2->next;
	}
	p1->next = NULL;
}

void SparseMatrix::insertElement(Element item)
{
	NodePtr prev = NULL;
	NodePtr curr = head;

	while(curr!=NULL && curr->e.isSmaller(item))
	{
		prev = curr;
		curr = curr->next;
	}

	NodePtr temp = new Node;
	temp -> e = item;

	// list empty
	if (head == NULL)
	{
		head = temp;
		temp->next = NULL;
	}
	// add front of the list
	else if (prev == NULL)
	{
		NodePtr temp2 = new Node;
		temp2 = head;
		temp->next = temp2;
		head = temp;
	}
	// add end of the list
	else if (curr == NULL)
	{
		temp -> next = NULL;
		curr = head;

		while (curr -> next != NULL)
			curr = curr -> next;
		curr -> next = temp;
	}
	// add middle of the list
	else
	{
		prev -> next = temp;
		temp -> next = curr;
	}

}
bool SparseMatrix::deleteElement(Element item)
{
	NodePtr prev = NULL;
	NodePtr curr = head;
	
	bool found = false, deleted = false;
	
	while (!found && curr!=NULL)
	{
		if (curr->e.isEqual(item))
			found = true;
		else
		{
			prev = curr;
			curr = curr -> next;
		}
	}
	// front of the list
	if (prev == NULL)
	{
		NodePtr temp = new Node;

		temp = head;
		head = head -> next;

		delete temp;
		deleted = true;
	}
	else
	{
		prev -> next = curr -> next;
		delete curr;
		deleted = true;
	}

	return deleted;
}
void SparseMatrix::printMatrix(int numarray[MAXROW][MAXCOL])const
{
	NodePtr curr = head;

	while(curr!=NULL)
	{
		int row = curr->e.getRow();
		int col = curr->e.getCol();
		int value = curr->e.getValue();
		numarray[row][col] = value;
		curr = curr->next;
	}
}
int SparseMatrix::findValue(Element item)
{
	NodePtr curr = head;

	while(curr!=NULL)
	{
		if(curr->e.isEqual(item))
			return curr->e.getValue();
		curr = curr->next;
	}
	return 0;
}
bool SparseMatrix::exist(Element item)
{
	NodePtr curr = head;

	while(curr!=NULL && !curr->e.isEqual(item))
		curr = curr->next;

	if(curr==NULL)
		return false;
	else 
		return true;
}

void SparseMatrix::operator+=(SparseMatrix &matrix)
{
	NodePtr p1 = NULL;
	NodePtr p2 = NULL;

	
	p2 = matrix.head;

	while(p2)
	{
		p1 = this->head;
		bool found = false;
		while(p1)
		{
			if(p1->e.isEqual(p2->e))
			{
				p1->e.setValue(p1->e.getValue()+p2->e.getValue());
				found = true;
			}
			p1 = p1->next;
		}

		if(!found)
			this->insertElement(p2->e);

		p2 = p2->next;
	}
}
void SparseMatrix::operator-=(SparseMatrix &matrix)
{
	NodePtr p1 = NULL;
	NodePtr p2 = NULL;

	
	p2 = matrix.head;

	while(p2)
	{
		p1 = this->head;
		bool found = false;
		while(p1)
		{
			if(p1->e.isEqual(p2->e))
			{
				p1->e.setValue(p1->e.getValue()-p2->e.getValue());
				found = true;
			}
			p1 = p1->next;
		}

		if(!found)
			this->insertElement(p2->e);

		p2 = p2->next;
	}
}
void SparseMatrix::operator*=(SparseMatrix &matrix)
{
	SparseMatrix copy(*this);
	Element el;
	
	NodePtr p1 = NULL;
	NodePtr p2 = NULL;
	NodePtr p3 = NULL;

	p2 = matrix.head;
	p3 = this->head;

	// copying of result to 'this' array
	while(p2)
	{
		p1 = copy.head;
		while(p1)
		{
			if(p2->e.getRow()==p1->e.getCol())
			{
				p3->next = new Node;
				p3 = p3->next;
				p3->e.set(p1->e.getRow(),p2->e.getCol());
				p3->e.setValue(p1->e.getValue()*p2->e.getValue());
				p3->next = NULL;
			}
			p1 = p1->next;
		}
		p2 = p2->next;
	}

	NodePtr temp = new Node;
	temp = head;
	head = head -> next;
	delete temp;
	
	cout << endl
		 << setw(60) << setfill('=')
		 << "="      << setfill(' ')
		 << endl;
}

void SparseMatrix::Sort(SparseMatrix &matrix)
{
	NodePtr p1 = NULL;
		
	head = new Node;
	head = NULL;

	p1 = matrix.head;
	while(p1!=NULL)
	{
		this->insertElement(p1->e);
		p1 = p1->next;
	}
}

