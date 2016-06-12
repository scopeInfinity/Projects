#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <fstream>


using namespace cv;
using namespace std;
int mouse_x[2], mouse_y[2];
int mouse_condition = 0;

//Filename to read image
char *fname = "9.jpg";
int rows, cols;
void getEdges();

//Code for curser in output starts
void CallBackMouse(int event, int x, int y, int flag, void *userdata)
{
	if (event == EVENT_LBUTTONDOWN)
	{
		if (mouse_condition < 2)
		{
			mouse_x[mouse_condition] = x;
			mouse_y[mouse_condition] = y;
			mouse_condition++;
			if (mouse_condition == 2)
			{
				getEdges();

			}
		}
		cout << "MOUSE " << x << ' ' << y << endl;
	}
}
// ends
void getColorImages(Mat image);

double CORNER_MIN_GAP;
/// Parameters for Shi-Tomasi algorithm
vector<Point2f> corners;

/// Global variables
Mat src, src_gray;

int maxCorners = 23;
int maxTrackbar = 100;
double edgeLen, edgeThres;
RNG rng(12345);
const char* source_window = "Image";

/// Function header
int getColorLine(int *, int, int, Point2f, Point2f);
//void goodFeaturesToTrack_Demo( int, void* );
double getDistance(Point2f, Point2f);
/**
* @function main
*/
int main()
{
	/// Load source image and convert it to gray
	src = imread(fname, 1);
	cvtColor(src, src_gray, COLOR_BGR2GRAY);
	CORNER_MIN_GAP = src.size().width / 20;
	/// Create Window
	namedWindow(source_window, WINDOW_AUTOSIZE);
	setMouseCallback(source_window, CallBackMouse, NULL);
	/// Create Trackbar to set the number of corners
	//createTrackbar( "Max  corners:", source_window, &maxCorners, maxTrackbar, goodFeaturesToTrack_Demo );
	//maxCorners = 96;
	imshow(source_window, src);


	//goodFeaturesToTrack_Demo( 0, 0 );
	waitKey(0);
	return 0;
}

double getDistance(Point2f a, Point2f b)
{
	return sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
}


void makeAttached(int *A, int w, int h, int i, int j, int c)
{
	if (*(A + i*w + j) == 1)
	{
		*(A + i*w + j) = c;
		makeAttached(A, w, h, i + 1, j, c);
		makeAttached(A, w, h, i, j + 1, c);
		makeAttached(A, w, h, i, j - 1, c);
		makeAttached(A, w, h, i - 1, j, c);
	}

}
class Edge
{
public:
	Point2f a, b;
	int cost = 1, color;
	int na, nb;
	Edge(int i, int j, Point2f A, Point2f B, int c)
	{
		na = i;
		nb = j;
		a.x = A.x;
		a.y = A.y;
		b.x = B.x;
		b.y = B.y;

		color = c;
		setCost();
	}
	Edge()
	{

	}
	int getNum(Point2f p)
	{
		if ((p.x == a.x && p.y == a.y))
			return na;
		if (p.x == b.x && p.y == b.y)
			return nb;
		return -1;
	}
	Edge(Edge &e){
		a = e.a;
		b = e.b;
		cost = e.cost;
		color = e.color;
		na = e.na;
		nb = e.nb;
	}
	void setCost()
	{
		cost = getCost(color);
	}
	static int getCost(int color)
	{
		int cost = 1e8;
		if (color == 0) cost = 25;
		else if (color == 1) cost = 20;
		else if (color == 2) cost = 10;
		else if (color == 3) cost = 15;
		return cost;
	}
	static int maxColor()
	{
		return 30;
	}
	int giveOther(int n)
	{
		if (na == n)
			return nb;
		if (nb == n)
			return na;
		return -1;
	}

};
void getEdges()
{
	IplImage* src = cvLoadImage(fname, 1); // Loading the image 
	IplImage* copy = cvCreateImage(cvGetSize(src), 8, 3); //Create a new image of,8 bit,3 channel 
	CvScalar s;  // create two scalar variables 
	int height = src->height, width = src->width;


	////////////CORNERS



	corners.clear();
	int *FC = new int[height*width];
	for (int i = 0; i <height; i++)
	for (int j = 0; j < width; j++)
	{
		s = cvGet2D(src, i, j); //Get the RGB values of src's i,j into a scalar s 
		//Detectiion OF WHITE INTERESECTIOn             ********************************************************************************

		if ((s.val[2]>150) && (s.val[1]>170) && (s.val[0]>180))
			FC[i*width + j] = 1;
		else 	FC[i*width + j] = 0;
	}

	//ALGO
	int c = 2;
	for (int i = 0; i <height; i++)
	for (int j = 0; j < width; j++)
	if (FC[i*width + j] == 1)
	{
		makeAttached(FC, width, height, i, j, c++);
	}
	cout << c << " Corners";
	//return;

	for (int num = 2; num < c; num++)
	{
		double x = 0, y = 0;
		int n = 0;
		for (int i = 0; i <height; i++)
		for (int j = 0; j < width; j++)
		if (FC[i*width + j] == num)
		{
			x += j; y += i; n++;
		}
		if (n == 0) continue;

		x /= n; y /= n;
		Point2f p;
		p.x = x;
		p.y = y;
		corners.push_back(p);
	}


	//ALGO for detecting corners ENDS
	delete[] FC;


	/// Draw corners detected
	// cout<<"** Number of corners detected: "<<corners.size()<<endl;
	int r = 4;
	for (size_t i = 0; i < corners.size(); i++)
	{
		for (size_t j = i + 1; j < corners.size(); j++)
		{
			if (getDistance(corners[i], corners[j]) < CORNER_MIN_GAP)
			{
				corners.erase(corners.begin() + j);
				j--;
			}
		}
	}
	edgeLen = getDistance(corners[0], corners[1]);
	for (size_t i = 0; i < corners.size(); i++)
	for (size_t j = i + 1; j < corners.size(); j++)
		edgeLen = min(edgeLen, getDistance(corners[i], corners[j]));
	edgeThres = edgeLen *0.4;







	Mat copy2;
	copy2 = (::src).clone();


	for (size_t i = 0; i < corners.size(); i++)
	{
		circle(copy2, corners[i], r, Scalar(255, 255, 0), -1, 8, 0);
	}

	/// Show what you got
	namedWindow(source_window, WINDOW_AUTOSIZE);
	//for ( size_t i = )
	imshow(source_window, copy2);
	









	/////////////////////CORNERS END



	int *MAT = new int[4 * height*width];

	for (int i = 0; i<height; i++)
	for (int j = 0; j<width; j++)
	{
		MAT[0 * width*height + i*width + j] = 0;
		MAT[1 * width*height + i*width + j] = 0;
		MAT[2 * width*height + i*width + j] = 0;
		MAT[3 * width*height + i*width + j] = 0;
		//Detectiion OF Edges             ********************************************************************************

		//RED
		s = cvGet2D(src, i, j); //Get the RGB values of src's i,j into a scalar s 
		if ((s.val[2]>150) && (s.val[1]<100) && (s.val[0]<100))
			MAT[0 * width*height + i*width + j] = 1;
		else


			//GREEN
		if ((s.val[2]<100) && (s.val[1]>100) && (s.val[0]<120))
			MAT[1 * width*height + i*width + j] = 1;
		else


			//YELLOW
		if ((s.val[2]>150) && (s.val[1]>150) && (s.val[0]<150))
			MAT[2 * width*height + i*width + j] = 1;
		else


			//BLUE
		if ((s.val[2]<100) && (s.val[1]<100) && (s.val[0]>100))
			MAT[3 * width*height + i*width + j] = 1;

	}
	c = 0;

	vector<Edge> edges;
	for (size_t i = 0; i < corners.size(); i++)
	for (size_t j = i + 1; j < corners.size(); j++)

	if (fabs(getDistance(corners[i], corners[j]) - edgeLen)<edgeThres)
	{
		int color = getColorLine(MAT, width, height, corners[i], corners[j]);
		if (color == -1) continue;
		//cout << color << ' ' << corners[i].x << ' ' << corners[i].y << ' ' << corners[j].x << ' ' << corners[j].y << endl;
		Scalar *s = NULL;
		if (color == 0)
			s = new Scalar(0, 0, 255);
		else if (color == 1)
			s = new Scalar(0, 255, 0);
		else if (color == 2)
			s = new Scalar(0, 255, 255);
		else if (color == 3)
			s = new Scalar(255, 0, 0);
		line(copy2, corners[i], corners[j], *s, 2);
		edges.push_back(Edge(i, j, corners[i], corners[j], color));
		delete s;
		c++;
	}
	imshow(source_window, copy2);

	/***************Link Edges********/
	/********** MINIMUM COST ALGO *************/

	int Corner_N_Start = 0;
	int Corner_N_End = 0;
	Point2f mouseS, mouseE;
	mouseS.x = mouse_x[0];
	mouseE.x = mouse_x[1];
	mouseS.y = mouse_y[0];
	mouseE.y = mouse_y[1];

	for (int i = 1; i < corners.size(); i++)
	{
		if (fabs(getDistance(corners[Corner_N_Start], mouseS))>fabs(getDistance(corners[i], mouseS)))
			Corner_N_Start = i;
		if (fabs(getDistance(corners[Corner_N_End], mouseE))>fabs(getDistance(corners[i], mouseE)))
			Corner_N_End = i;

	}
	//	for (int i = 0; i < edges.size(); i++)
	//	cout << edges[i].na << " " << edges[i].nb <<','<<edges[i].giveOther(2)<< endl;

	vector<int> isDone(corners.size(), 0);
	int MAX = edges.size() * Edge::maxColor();
	vector<int> minCost(corners.size(), MAX);
	vector<int> lastCorner(corners.size(), -1);

	//make initally minCost = 0
	minCost[Corner_N_Start] = 0;
	int aa = 0;
	while (1)
	{
		int minCorner = -1, minVAL = MAX;
		for (int j = 0; j<corners.size(); j++)
		if (minVAL >= minCost[j] && isDone[j] == 0)
		{
			minCorner = j; minVAL = minCost[j];
		}
		//cout << " M" << minCorner;
		if (minCorner == -1) break;
		isDone[minCorner] = 1;
		//for (size_t j = 0; j < edges.size(); j++)
		for (int j = 0; j < corners.size(); j++)
		{

			//cout << j << "> " << edges[j].na << ' ' << edges[j].nb << ';' <<minCorner<<": "<< edges[j].giveOther(minCorner)<<endl;
			if (fabs(getDistance(corners[minCorner], corners[j]) - edgeLen)<edgeThres)
				//if (edges[j].giveOther(minCorner) >= 0)
			{

				int other = j;
				//int other = edges[j].giveOther(minCorner);
				//if (other != -1)
				if (!isDone[other])
				{

					int cost = Edge::getCost(getColorLine(MAT, width, height, corners[minCorner], corners[j]));
					if (cost == 0) cost = MAX;
					cout << cost << ' ';
					if (minCost[minCorner] + cost <= minCost[other])
					{
						minCost[other] = minCost[minCorner] + cost;// edges[j].cost;
						lastCorner[other] = minCorner;
						//cout << "SET MIN " << other << " : " << minCost[other] << endl;

					}

				}
			}
		}
	}
	int current = Corner_N_End;
	cout << " PATH" << endl;
	vector<int> path;
	CvFont font;
	cvInitFont(&font, CV_FONT_HERSHEY_SIMPLEX, 0.5, 0.5);

	while (current != -1 && current != Corner_N_Start)
	{
		path.push_back(current);
		cout << minCost[current] << '<'; // min Cost[current]-for cost of path and current - for vertex of path
		String s = to_string(current);
		//	addText(copy2, s, corners[current], font);
		current = lastCorner[current];
	}
	cout << Corner_N_Start;
	path.push_back(Corner_N_Start);


	for (int i = 0; i < path.size() - 1; i++)
		line(copy2, corners[path[i]], corners[path[i + 1]], Scalar(255, 255, 255), 5);
	imshow(source_window, copy2);

	cout << "DONE\n" << edges[2].color << ' ' << edges[2].cost;




	/********************************************** MOVING CONTROLS ******************/

	/****
	0  UP
	1  RIGHT
	2  DOWN
	3 LEFT
	*****/
	ofstream fout("moves.txt");
	fout << "int Path[]={";
	int direction = 0;
	for (int i = path.size() - 2; i >= 0; i--)
	{
		int motion = 0;//0 - fwd, -1 anticlock, +1 clockwise 
		double delta_x = corners[path[i]].x - corners[path[i + 1]].x;
		double delta_y = corners[path[i]].y - corners[path[i + 1]].y;
		int new_direction;
		if (fabs(delta_x)>fabs(delta_y))
		{
			delta_y = 0;
		}
		else
		{
			delta_x = 0;
		}
		if (delta_x == 0)
		{
			if (delta_y > 0) new_direction = 2;
			else new_direction = 0;
		}
		else {
			if (delta_x > 0) new_direction = 1;
			else new_direction = 3;

		}
		//cout << ">   " <<delta_x<<','<<delta_y<<','<< new_direction<< "<";
		if (new_direction == 1){
			if (direction == 0)
				fout << "1,";
			if (direction == 2)
				fout << "-1,";
			if (direction == 3)
				fout << "-1,-1,";

		}
		else if (new_direction == 2){
			if (direction == 0)
				fout << "1,1,";
			if (direction == 1)
				fout << "1,";
			if (direction == 3)
				fout << "-1,";

		}
		else if (new_direction == 3){
			if (direction == 0)
				fout << "-1,";
			if (direction == 1)
				fout << "1,1,";
			if (direction == 2)
				fout << "1,";

		}
		else if (new_direction == 0){
			if (direction == 1)
				fout << "-1,";
			if (direction == 2)
				fout << "1,1,";
			if (direction == 3)
				fout << "1,";

		}
		fout << "0,";
		direction = new_direction;

	}

	fout << "100};\n";
	fout.close();








	/********************************************************************************/








	/***********MINIMUM COST ALGO ENDS******************************/
	delete[] MAT;
}
int getColorLine(int *MAT, int width, int height, Point2f a, Point2f b)
{
	int MAX = 10;	/**********************SAMPLE POINTS ON LINE **************************************************************/
	int A[4] = { 0 };
	for (int i = 1; i < MAX; i++)
	{
		double x = (a.x*i + b.x*(MAX - i)) / MAX;
		double y = (a.y*i + b.y*(MAX - i)) / MAX;
		for (int j = 0; j < 4; j++)
		{
			if (*(MAT + j*width*height + ((int)y)*width + (int)x))
			{
				A[j]++;
			}

		}
	}
	int in = 0;
	int flag = 0;

	for (int i = 1; i < 4; i++)
	if (A[in] < A[i]) in = i;
	//	cout << A[0] << ":" << A[1] << ":" << A[2] << ":" << A[3];
	if (A[in] == 0)
		return -1;
	return in;
}




