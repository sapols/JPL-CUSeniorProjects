Master: [![Build Status](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects.svg?branch=master)](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects)
Dev: [![Build Status](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects.svg?branch=dev)](https://travis-ci.org/RossBlassingame/JPL-CUSeniorProjects)

# CU Senior Projects -- JPL Team

## M.A.R.S.
The Martian Autonomous Routing System (or MARS) is a command-line utility that produces lists of coordinates describing possible routes for a rover to take to reach a goal coordinate from the start, whilst navigating a provided elevation map, within the bounds of its physical capabilities.

## Members:
 - Ross Blassingame
 - Robert Ballard
 - Shawn Polson
 - Josh Jenkins
 - Chandler Samuels
 - Dean Moser
 
## Sponsor
This project is sponsored by NASA Jet Propulsion Laboratory, with Marcel Llopis providing advice and oversight for the work.

## Usage

### Input

To use the program, you must provide it:
- a compatible elevation map (see below)
- specifications for a theoretical rover, including a maximum slope it can safely traverse, as well as field of view for using the limited route algorithm
- a start coordinate (in pixels) corresponding to a location on the input map
- an end coordinate (in pixels) also corresponding to the map
- a specified algorithm type to run (see below)
- a specified algorithm to run (see below)
- a specified coordinate output type (see below)
- a specified output type (see below)


Compatible elevation map formats include:

| Format    | Accepted Extension(s) |
| --------- | --------------------- |
| GeoTIFF   | .tiff, .tif           |

Available maps include:

| Map Names                                  | Prompt Key  | Description           |
| -----------------------------------------  | ----------- | ----------------------|
| Mars_MGS_MOLA_DEM_mosaic_global_463m.tif   | (1)         | 128 Pixels Per Degree |
| Phobos_ME_HRSC_DEM_Global_2ppd.tiff        | (2)         | 2 Pixels Per Degree   |
| Phobos_Viking_Mosaic_40ppd_DLRcontrol.tif  | (3)         | 40 Pixels Per Degree  |

Available algorithm types include:

| Algorithm Type  | Prompt Key | Description                                                                               |
| --------------- | ---------- | ----------------------------------------------------------------------------------------- |
| Unlimited scope | U          | Navigates from start to goal with perfect information with regards to the elevation map.  |
| Limited scope   | L          | Navigates from start to goal with information gathered as it traverses the elevation map. |
 
Available algorithms include:

| Unlimited Algorithms| Prompt Key | Description                                                                               |
| ------------------- | ---------- | ----------------------------------------------------------------------------------------- |
| Greedy              | (1)        | Navigates from start to goal with perfect information with regards to the elevation map.  |
| IDA*                | (2)        | Navigates from start to goal with information gathered as it traverses the elevation map. |
| Best First          | (3)        | Navigates from start to goal with perfect information with regards to the elevation map.  |
| Breadth First Search| (4)        | Navigates from start to goal with information gathered as it traverses the elevation map. | 
| A* (Non-Recursive)  | (5)        | Navigates from start to goal with perfect information with regards to the elevation map.  |
| A* (Recursive)      | (6)        | Navigates from start to goal with information gathered as it traverses the elevation map. | 
| Dijkstra            | (7)        | Navigates from start to goal with information gathered as it traverses the elevation map. |

| Limited Algorithms  | Prompt Key | Description                                                                               |
| ------------------- | ---------- | ----------------------------------------------------------------------------------------- |
| Greedy              | (1)        | Navigates from start to goal with perfect information with regards to the elevation map.  |
| IDA*                | (2)        | Navigates from start to goal with information gathered as it traverses the elevation map. |
| Best First          | (3)        | Navigates from start to goal with perfect information with regards to the elevation map.  |
| Breadth First Search| (4)        | Navigates from start to goal with information gathered as it traverses the elevation map. | 
| A* (Non-Recursive)  | (5)        | Navigates from start to goal with perfect information with regards to the elevation map.  |
| A* (Recursive)      | (6)        | Navigates from start to goal with information gathered as it traverses the elevation map. | 
| Dijkstra            | (7)        | Navigates from start to goal with information gathered as it traverses the elevation map. |

Available output coordinate types include:

| Coordinate Type| Prompt Key | Description                                                                                |
| -------------- | ---------- | ------------------------------------------------------------------------------------------ |
| Pixel          | P          | Outputs the rover's path from start to goal coordinate in pixels.                          |
| Lat/Lon        | L          | Outputs the rover's path from start to goal coordinate in latitude and longitude.          |

Available output options include:

| Output Type| Prompt Key | Description                                                                                    |
| -------------- | ---------- | ------------------------------------------------------------------------------------------ |
| File Output    | (1)        | Outputs the rover's path from start to goal coordinate in an external CSV file.            |
| Image Output   | (2)        | Outputs the rover's path from start to goal coordinate by coloring the path on the GeoTIFF.|
| Terminal Output| (3)        | Outputs the rover's path from start to goal coordinate in the terminal.                    |

This information can be provided to the utiltity by simply running it, and following the prompts. 
 
### Execution

Once the program completes, the resultant path the chosen algorithm determined will be output to the terminal in a human-readable format. It can be run again to check against another algorithm or saved to a CSV.

## Copyright

something open source TODO
